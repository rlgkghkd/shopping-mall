package com.example.shoppingmall.comment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.comment.dto.request.CreateCommentRequestDto;
import com.example.shoppingmall.comment.dto.response.CreateCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.FindByAllCommentResponseDto;
import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.repository.OrderRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;

	public CreateCommentResponseDto createComment(CreateCommentRequestDto createCommentRequestDto) {

		Long itemId = 1L; // 추후에 지울거예요   // ^^;; order.getId()
		Long userId = 1L; // 추후에 지울거예요   // JWT 유저ID - order.getUserId();  비교용

		Order order = getOrderOrThrow(createCommentRequestDto.getOrderId());
		Item item = getItemOrThrow(itemId);
		User user = getUserOrThrow(userId);

		if (createCommentRequestDto.getCommentId() == null) {

			Comment comment = new Comment(
				createCommentRequestDto.getContent(),
				user,
				item,
				order
			);

			Comment createdComment = commentRepository.save(comment);

			return new CreateCommentResponseDto(
				createdComment.getId(),
				createdComment.getOrder().getId(),
				createdComment.getUser().getId(),
				createdComment.getContent(),
				createdComment.getCreatedAt()
			);

		} else {
			Comment parentComment = getCommentOrThrow(createCommentRequestDto.getCommentId());

			if (parentComment.getReply() != null) {
				throw new IllegalArgumentException("이미 답글이 존재합니다.");
			}

			// reply 먼저 저장
			Comment reply = new Comment(
				createCommentRequestDto.getContent(),
				user,
				item,
				order
			);
			reply.setParentComment(parentComment);
			Comment savedReply = commentRepository.save(reply);

			// 부모 댓글에 reply 연결
			parentComment.setReply(savedReply);
			commentRepository.save(parentComment);

			return new CreateCommentResponseDto(
				reply.getId(),
				reply.getOrder().getId(),
				reply.getUser().getId(),
				reply.getContent(),
				reply.getCreatedAt()
			);
		}

	}

	public List<FindByAllCommentResponseDto> findByAllComment() {
		// 부모 댓글만 조회
		List<Comment> parentComments = commentRepository.findByparentCommentIsNull();
		// 조회된 내용 닮을 리스트 생성
		List<FindByAllCommentResponseDto> findByAllCommentList = new ArrayList<>();

		for (Comment parent : parentComments) {

			// 자식 댓글을 초기화
			CreateCommentResponseDto replyDto = null;

			// 부모 댓글에 reply가 있으면 reply DTO 생성
			if (parent.getReply() != null) {
				Comment reply = parent.getReply();
				replyDto = new CreateCommentResponseDto(
					reply.getId(),
					reply.getOrder().getId(),
					reply.getUser().getId(),
					reply.getContent(),
					reply.getCreatedAt()
				);
			}

			FindByAllCommentResponseDto parentDto = new FindByAllCommentResponseDto(
				parent.getId(),
				parent.getOrder().getId(),
				parent.getUser().getId(),
				parent.getContent(),
				parent.getCreatedAt(),
				replyDto
			);

			findByAllCommentList.add(parentDto);
		}

		return findByAllCommentList;

	}

	public User getUserOrThrow(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
	}

	public Item getItemOrThrow(Long itemId) {
		return itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
	}

	public Order getOrderOrThrow(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문 내역을 찾을 수 없습니다."));
	}

	public Comment getCommentOrThrow(Long commentId) {
		return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
	}
}

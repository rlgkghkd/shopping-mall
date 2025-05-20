package com.example.shoppingmall.comment.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.comment.dto.request.CreateCommentRequestDto;
import com.example.shoppingmall.comment.dto.request.UpdateCommentRequestDto;
import com.example.shoppingmall.comment.dto.response.CreateCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.DeleteCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.FindByAllCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.FindByIdCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.UpdateCommentResponseDto;
import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.exception.CommentErrorCode;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.repository.OrderRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;

	public CreateCommentResponseDto createComment(
		CreateCommentRequestDto createCommentRequestDto,
		CustomUserDetails customUserDetails) {

		User user = getUserOrThrow(customUserDetails.getUserId());
		Order order = getOrderOrThrow(createCommentRequestDto.getOrderId());
		Item item = getItemOrThrow(order.getItem().getId());

		if (createCommentRequestDto.getCommentId() == null) {

			// 로그인한 유저와 주문한 유저가 다르면 예외 처리
			if (!order.getUser().getId().equals(user.getId())) {
				throw new CustomException(CommentErrorCode.NAUTHORIZED_COMMENT_CREATION);
			}

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
				createdComment.getUser().getUsername(),
				createdComment.getContent(),
				createdComment.getCreatedAt()
			);

		} else {
			// 관리자가 아니면 예외 처리
			if (user.getUserRole() != UserRole.ADMIN) {
				throw new CustomException(CommentErrorCode.UNAUTHORIZED_REPLY_CREATION);
			}

			Comment parentComment = getCommentOrThrow(createCommentRequestDto.getCommentId());

			if (parentComment.getReply() != null) {
				throw new CustomException(CommentErrorCode.REPLY_ALREADY_EXISTS);
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
				reply.getUser().getUsername(),
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
					reply.getUser().getUsername(),
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
				parent.getUpdatedAt(),
				replyDto
			);

			findByAllCommentList.add(parentDto);
		}

		return findByAllCommentList;

	}

	public FindByIdCommentResponseDto findByComment(Long id, CustomUserDetails customUserDetails) {
		Comment comment = getCommentOrThrow(id);
		User user = getUserOrThrow(customUserDetails.getUserId());

		boolean isWriter = comment.getUser().getId().equals(user.getId());
		boolean isAdmin = user.getUserRole() == UserRole.ADMIN;

		if (!isWriter && !isAdmin) {
			throw new CustomException(CommentErrorCode.UNAUTHORIZED_COMMENT_VIEW);
		}

		// 답글이 존재하면 DTO로 변환
		CreateCommentResponseDto replyDto = null;

		if (comment.getReply() != null) {
			Comment reply = comment.getReply();

			replyDto = new CreateCommentResponseDto(
				reply.getId(),
				reply.getOrder().getId(),
				reply.getUser().getUsername(),
				reply.getContent(),
				reply.getCreatedAt()
			);
		}

		return new FindByIdCommentResponseDto(
			comment.getId(),
			comment.getOrder().getId(),
			comment.getUser().getId(), // 추후에 username으로 변경 가능
			comment.getContent(),
			comment.getCreatedAt(),
			comment.getUpdatedAt(),
			replyDto
		);

	}

	@Transactional
	public UpdateCommentResponseDto updateComment(
		Long id,
		UpdateCommentRequestDto updateCommentRequestDto,
		CustomUserDetails customUserDetails
	) {
		User user = getUserOrThrow(customUserDetails.getUserId());
		Comment comment = getCommentOrThrow(id);

		if (!comment.getUser().getId().equals(user.getId())) {
			throw new CustomException(CommentErrorCode.UNAUTHORIZED_COMMENT_UPDATE);
		}

		comment.UpdateComment(updateCommentRequestDto);
		return new UpdateCommentResponseDto(
			comment.getId(),
			comment.getOrder().getId(),
			comment.getUser().getId(),
			comment.getContent(),
			comment.getCreatedAt(),
			comment.getUpdatedAt()
		);
	}

	public DeleteCommentResponseDto deleteComment(Long id, CustomUserDetails customUserDetails) {
		Comment comment = getCommentOrThrow(id);
		User user = getUserOrThrow(customUserDetails.getUserId());

		boolean isWriter = comment.getUser().getId().equals(user.getId());
		boolean isAdmin = user.getUserRole() == UserRole.ADMIN;

		if (!isWriter && !isAdmin) {
			throw new CustomException(CommentErrorCode.UNAUTHORIZED_COMMENT_DELETE);
		}

		if (comment.getParentComment() != null) {
			Comment parentComment = getCommentOrThrow(comment.getParentComment().getId());
			parentComment.setReply(null);
			comment.setParentComment(null);
		}
		commentRepository.delete(comment);
		return new DeleteCommentResponseDto("댓글이 삭제 되었습니다.");
	}

	public User getUserOrThrow(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new CustomException(CommentErrorCode.USER_NOT_FOUND));
	}

	public Item getItemOrThrow(Long itemId) {
		return itemRepository.findById(itemId).orElseThrow(() -> new CustomException(CommentErrorCode.ITEM_NOT_FOUND));
	}

	public Order getOrderOrThrow(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(CommentErrorCode.ORDER_NOT_FOUND));
	}

	public Comment getCommentOrThrow(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
	}

}

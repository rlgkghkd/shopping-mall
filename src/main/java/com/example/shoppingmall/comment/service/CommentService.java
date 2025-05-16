package com.example.shoppingmall.comment.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.comment.dto.request.CreateCommentRequestDto;
import com.example.shoppingmall.comment.dto.response.CreateCommentResponseDto;
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
			Comment comment = getCommentOrThrow(createCommentRequestDto.getCommentId());
			if (comment.getReply() != null) {
				throw new IllegalArgumentException("이미 답글이 존재합니다.");
			}

			Comment reply = new Comment(
				createCommentRequestDto.getContent(),
				user,
				item,
				order
			);

			comment.setReply(reply);
			Comment createdReply = commentRepository.save(reply);

			return new CreateCommentResponseDto(
				createdReply.getId(),
				createdReply.getOrder().getId(),
				createdReply.getUser().getId(),
				createdReply.getContent(),
				createdReply.getCreatedAt()
			);
		}

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

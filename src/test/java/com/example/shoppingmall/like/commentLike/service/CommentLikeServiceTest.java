package com.example.shoppingmall.like.commentLike.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.repository.CommentRepository;
import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.like.commentLike.dto.LeaveCommentLikeResponseDto;
import com.example.shoppingmall.like.commentLike.entity.CommentLike;
import com.example.shoppingmall.like.commentLike.repository.CommentLikeRepository;
import com.example.shoppingmall.like.exception.LikesErrors;
import com.example.shoppingmall.order.entity.Order;
import com.example.shoppingmall.order.type.OrderStatus;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CommentLikeServiceTest {

	@InjectMocks
	private CommentLikeService commentLikeService; // 테스트할 서비스 클래스

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CommentLikeRepository commentLikeRepository;

	@Mock
	private CustomUserDetails userDetails;

	@Nested
	class create {
		@Test
		void 댓글_좋아요_성공() {
			// given
			Long commentId = 1L;
			User user = new User("testname", "test@mail", UserRole.ADMIN, "testPass@");
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 0L);
			Order order = new Order(1L, user, item, "testAddr", OrderStatus.PENDING, 1234);
			Comment comment = new Comment("testContent", user, item, order);
			given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

			given(userRepository.findById(userDetails.getUserId())).willReturn(Optional.of(user));

			CommentLike commentLike = new CommentLike(comment, user);
			given(commentLikeRepository.searchLikeByUserAndComment(user.getId(), comment)).willReturn(false);
			given(commentLikeRepository.save(any(CommentLike.class))).willReturn(commentLike);

			// when
			LeaveCommentLikeResponseDto response = commentLikeService.leaveLikeOnComment(commentId, userDetails);

			// then
			assertNotNull(response);
			assertNull(response.getLikedContentId());
			assertEquals(user.getId(), response.getLikeId());
		}

		@Test
		void 이미_좋아요_한_댓글() {
			// given
			Long commentId = 1L;
			User user = new User("testname", "test@mail", UserRole.ADMIN, "testPass@");
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 0L);
			Order order = new Order(1L, user, item, "testAddr", OrderStatus.PENDING, 1234);
			Comment comment = new Comment("testContent", user, item, order);
			given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

			given(userRepository.findById(userDetails.getUserId())).willReturn(Optional.of(user));

			given(commentLikeRepository.searchLikeByUserAndComment(user.getId(), comment)).willReturn(true);

			// when
			CustomException exception = assertThrows(
				CustomException.class, () -> commentLikeService.leaveLikeOnComment(commentId, userDetails));

			// then
			assertEquals(LikesErrors.ALREADY_LIKED, exception.getErrors());
			assertEquals("이미 좋아요 했습니다.", exception.getMessage());
		}
	}

	@Nested
	class delete {
		@Test
		void 댓글_좋아요_취소() {
			// given
			Long likeId = 1L;

			User user = new User(1L, "testname", "test@mail", "testPass@", UserRole.ADMIN, LocalDateTime.now(), false);
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 0L);
			Order order = new Order(1L, user, item, "testAddr", OrderStatus.PENDING, 1234);
			given(userRepository.findById(userDetails.getUserId())).willReturn(Optional.of(user));

			Comment comment = new Comment("testContent", user, item, order);
			comment.increaseLikeCount(1L);
			CommentLike commentLike = new CommentLike(comment, user);
			given(commentLikeRepository.findById(likeId)).willReturn(Optional.of(commentLike));

			// when
			commentLikeService.deleteLikeOnComment(likeId, userDetails);

			// then
			verify(commentLikeRepository).delete(commentLike);
			assertEquals(0L, comment.getLikeCount());
		}

		@Test
		void 다른_유저의_좋아요() {
			// given
			Long likeId = 1L;

			User user = new User(1L, "testname", "test@mail", "testPass@", UserRole.ADMIN, LocalDateTime.now(), false);
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 0L);
			Order order = new Order(1L, user, item, "testAddr", OrderStatus.PENDING, 1234);
			given(userRepository.findById(userDetails.getUserId())).willReturn(Optional.of(user));

			User otherUser = new User(2L, "testname", "test@mail", "testPass@", UserRole.ADMIN, LocalDateTime.now(),
				false);
			Comment comment = new Comment("testContent", user, item, order);
			CommentLike commentLike = new CommentLike(comment, otherUser);
			given(commentLikeRepository.findById(1L)).willReturn(Optional.of(commentLike));

			// when
			CustomException exception = assertThrows(CustomException.class,
				() -> commentLikeService.deleteLikeOnComment(likeId, userDetails));

			// then
			assertEquals(LikesErrors.OTHER_USERS_LIKE, exception.getErrors());
			assertEquals("본인이 남긴 좋아요가 아닙니다.", exception.getMessage());
		}
	}
}

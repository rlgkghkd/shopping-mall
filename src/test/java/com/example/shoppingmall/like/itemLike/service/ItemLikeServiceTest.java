package com.example.shoppingmall.like.itemLike.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.exception.LikesErrors;
import com.example.shoppingmall.like.itemLike.dto.LeaveItemLikeResponseDto;
import com.example.shoppingmall.like.itemLike.entity.ItemLike;
import com.example.shoppingmall.like.itemLike.repository.ItemLikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.exception.UserErrorCode;
import com.example.shoppingmall.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class ItemLikeServiceTest {

	@InjectMocks
	private ItemLikeService itemLikeService; // 테스트할 서비스 클래스

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ItemLikeRepository itemLikeRepository;

	@Mock
	private HttpServletRequest request;

	@Nested
	class create {
		@Test
		void 상품_좋아요_성공() {
			// given
			Long itemId = 1L;
			String token = "valid-token";
			String userEmail = "test@example.com";

			Claims claims = mock(Claims.class);
			given(claims.get("email", String.class)).willReturn(userEmail);

			User user = new User("testname", "test@mail", UserRole.ADMIN, "testPass@");
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 0L);
			ItemLike itemLike = new ItemLike(item, user);

			given(jwtUtil.subStringToken(request)).willReturn(token);
			given(jwtUtil.extractClaim(token)).willReturn(claims);
			given(itemRepository.findById(itemId)).willReturn(Optional.of(item));
			given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));
			given(itemLikeRepository.searchLikeByUserAndItem(user.getId(), item)).willReturn(false);
			given(itemLikeRepository.save(any(ItemLike.class))).willReturn(itemLike);

			// when
			LeaveItemLikeResponseDto response = itemLikeService.leaveLikeOnItem(itemId, request);

			// then
			assertNotNull(response);
			assertEquals(itemId, response.getLikedContentId());
			assertEquals(user.getId(), response.getLikeId());
		}

		@Test
		void 이미_좋아요_한_상품() {
			// given
			Long itemId = 1L;
			String token = "valid-token";
			String userEmail = "test@example.com";

			Claims claims = mock(Claims.class);
			given(claims.get("email", String.class)).willReturn(userEmail);

			User user = new User("testname", "test@mail", UserRole.ADMIN, "testPass@");
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 0L);
			ItemLike itemLike = new ItemLike(item, user);

			given(jwtUtil.subStringToken(request)).willReturn(token);
			given(jwtUtil.extractClaim(token)).willReturn(claims);
			given(itemRepository.findById(itemId)).willReturn(Optional.of(item));
			given(userRepository.findByEmail(userEmail)).willReturn(Optional.of(user));
			given(itemLikeRepository.searchLikeByUserAndItem(user.getId(), item)).willReturn(true);

			// when
			CustomException exception = assertThrows(
				CustomException.class, () -> itemLikeService.leaveLikeOnItem(itemId, request));

			// then
			assertEquals(LikesErrors.ALREADY_LIKED, exception.getErrors());
			assertEquals("이미 좋아요 했습니다.", exception.getMessage());
		}

		@Test
		void 잘못된_토큰() {
			// given
			Long itemId = 1L;

			given(jwtUtil.subStringToken(request)).willReturn(null);

			// when
			ResponseStatusException exception = assertThrows(
				ResponseStatusException.class, () -> itemLikeService.leaveLikeOnItem(itemId, request));

			// then
			assertEquals("400 BAD_REQUEST", exception.getMessage());
		}
	}

	@Nested
	class delete {
		@Test
		void 상품_좋아요_취소() {
			// given
			Long likeId = 1L;
			String token = "valid-token";
			given(jwtUtil.subStringToken(request)).willReturn(token);

			Claims claims = mock(Claims.class);
			given(jwtUtil.extractClaim(token)).willReturn(claims);

			String email = "test@mail.com";
			given(claims.get("email", String.class)).willReturn(email);

			User user = new User(1L, "testname", "test@mail", "testPass@", UserRole.ADMIN, LocalDateTime.now(), false);
			given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 1L);
			ItemLike itemLike = new ItemLike(item, user);
			given(itemLikeRepository.findById(likeId)).willReturn(Optional.of(itemLike));

			// when
			itemLikeService.deleteLikeOnItem(likeId, request);

			// then
			verify(itemLikeRepository).delete(itemLike);
			assertEquals(0L, item.getLikeCount());
		}

		@Test
		void 다른_유저의_좋아요() {
			// given
			Long likeId = 1L;
			String token = "valid-token";
			given(jwtUtil.subStringToken(request)).willReturn(token);

			Claims claims = mock(Claims.class);
			given(jwtUtil.extractClaim(token)).willReturn(claims);

			String email = "test@mail.com";
			given(claims.get("email", String.class)).willReturn(email);

			User user = new User(1L, "testname", "test@mail", "testPass@", UserRole.ADMIN, LocalDateTime.now(), false);
			given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

			User otherUser = new User(2L, "testname", "test@mail", "testPass@", UserRole.ADMIN, LocalDateTime.now(),
				false);
			Item item = new Item(1L, "testItem", "testContent", 123, Category.Beauty, 1L);
			ItemLike itemLike = new ItemLike(item, otherUser);
			given(itemLikeRepository.findById(likeId)).willReturn(Optional.of(itemLike));

			// when
			CustomException exception = assertThrows(CustomException.class,
				() -> itemLikeService.deleteLikeOnItem(likeId, request));

			// then
			assertEquals(LikesErrors.OTHER_USERS_LIKE, exception.getErrors());
			assertEquals("본인이 남긴 좋아요가 아닙니다.", exception.getMessage());
		}

		@Test
		void 유저_메일_없음() {
			// given
			Long likeId = 1L;
			String token = "valid-token";
			given(jwtUtil.subStringToken(request)).willReturn(token);

			Claims claims = mock(Claims.class);
			given(jwtUtil.extractClaim(token)).willReturn(claims);

			String email = "test@mail.com";
			given(claims.get("email", String.class)).willReturn(null);

			// when
			CustomException exception = assertThrows(CustomException.class,
				() -> itemLikeService.deleteLikeOnItem(likeId, request));

			// then
			assertEquals(UserErrorCode.NOT_FOUND_USER, exception.getErrors());
			assertEquals("사용자 정보가 없습니다.", exception.getMessage());
		}
	}
}

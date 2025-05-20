package com.example.shoppingmall.like.itemLike.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.exception.AlreadyLikedException;
import com.example.shoppingmall.like.exception.NoTokenException;
import com.example.shoppingmall.like.exception.WrongUsersLikeException;
import com.example.shoppingmall.like.itemLike.dto.LeaveItemLikeResponseDto;
import com.example.shoppingmall.like.itemLike.entity.ItemLike;
import com.example.shoppingmall.like.itemLike.repository.ItemLikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemLikeService {
	private final ItemLikeRepository itemLikeRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public LeaveItemLikeResponseDto leaveLikeOnItem(Long itemId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		if (token == null) {
			throw new NoTokenException("토큰이 존재하지 않습니다.");
		}
		Claims claims = jwtUtil.extractClaim(token);

		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item 없음"));
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user 없음"));

		if (itemLikeRepository.searchLikeByUserAndItem(user.getId(), item)) {
			throw new AlreadyLikedException("이미 좋아요 한 상품입니다.");
		}

		ItemLike itemLike = new ItemLike(item, user);
		ItemLike saved = itemLikeRepository.save(itemLike);
		item.increaseLikeCount(1L);

		LeaveItemLikeResponseDto responseDto = new LeaveItemLikeResponseDto(saved.getItem().getClass().toString(),
			saved.getItem().getId(), saved.getId(), saved.getCreatedAt());
		return responseDto;
	}

	@DistributedLock(key = "delete Like")
	public void deleteLikeOnItem(Long likeId, HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);
		if (token == null) {
			throw new NoTokenException("토큰이 존재하지 않습니다.");
		}

		Claims claims = jwtUtil.extractClaim(token);
		String userMail = claims.get("email", String.class);
		if (userMail == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token에 email 없음");
		}

		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user 없음"));

		ItemLike like = itemLikeRepository.findById(likeId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ItemLike 없음"));
		Item item = like.getItem();

		if (!like.getUser().getId().equals(user.getId())) {
			throw new WrongUsersLikeException("본인이 남긴 좋아요가 아닙니다.");
		}

		itemLikeRepository.delete(like);
		item.decreaseLikeCount(1L);
		itemRepository.save(item);
	}
}

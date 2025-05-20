package com.example.shoppingmall.like.itemLike.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
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
		Claims claims = jwtUtil.extractClaim(token);

		Item item = itemRepository.findById(itemId).orElseThrow();
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (itemLikeRepository.searchLikeByUserAndItem(user.getId(), item)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
		Claims claims = jwtUtil.extractClaim(token);
		String userMail = claims.get("email", String.class);
		User user = userRepository.findByEmail(userMail)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		ItemLike like = itemLikeRepository.findById(likeId).orElseThrow();
		Item item = like.getItem();

		if (!like.getUser().getId().equals(user.getId())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		itemLikeRepository.delete(like);
		item.decreaseLikeCount(1L);
		itemRepository.save(item);
	}
}

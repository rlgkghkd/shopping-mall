package com.example.shoppingmall.like.itemLike.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.common.DistributedLock;
import com.example.shoppingmall.common.exception.CustomException;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.repository.ItemRepository;
import com.example.shoppingmall.like.exception.LikesErrors;
import com.example.shoppingmall.like.itemLike.dto.LeaveItemLikeResponseDto;
import com.example.shoppingmall.like.itemLike.entity.ItemLike;
import com.example.shoppingmall.like.itemLike.repository.ItemLikeRepository;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.exception.UserErrorCode;
import com.example.shoppingmall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemLikeService {
	private final ItemLikeRepository itemLikeRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	@Transactional
	public LeaveItemLikeResponseDto leaveLikeOnItem(Long itemId, CustomUserDetails userDetails) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "item 없음"));

		User user = userRepository.findById(userDetails.getUserId())
			.orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));

		if (itemLikeRepository.searchLikeByUserAndItem(user.getId(), item)) {
			throw new CustomException(LikesErrors.ALREADY_LIKED);
		}

		ItemLike itemLike = new ItemLike(item, user);
		ItemLike saved = itemLikeRepository.save(itemLike);
		item.increaseLikeCount(1L);

		LeaveItemLikeResponseDto responseDto = new LeaveItemLikeResponseDto(
			saved.getItem().getClass().getSimpleName(),
			saved.getItem().getId(),
			saved.getId(),
			saved.getCreatedAt());
		return responseDto;
	}

	@DistributedLock(key = "delete Like")
	public void deleteLikeOnItem(Long likeId, CustomUserDetails userDetails) {

		User user = userRepository.findById(userDetails.getUserId())
			.orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));

		ItemLike like = itemLikeRepository.findById(likeId)
			.orElseThrow(() -> new CustomException(LikesErrors.NOT_FOUND_LIKE));

		Item item = like.getItem();

		if (!like.getUser().getId().equals(user.getId())) {
			throw new CustomException(LikesErrors.OTHER_USERS_LIKE);
		}

		itemLikeRepository.delete(like);
		item.decreaseLikeCount(1L);
		itemRepository.save(item);
	}
}

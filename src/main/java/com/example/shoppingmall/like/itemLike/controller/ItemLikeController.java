package com.example.shoppingmall.like.itemLike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.like.itemLike.dto.LeaveItemLikeResponseDto;
import com.example.shoppingmall.like.itemLike.service.ItemLikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemLikeController {
	private final ItemLikeService itemLikeService;

	@PostMapping("/items/{itemId}/likes")
	public ResponseEntity<LeaveItemLikeResponseDto> leaveLikeOnItem(@PathVariable Long itemId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(itemLikeService.leaveLikeOnItem(itemId, userDetails));
	}

	@DeleteMapping("/items/{itemId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnItem(@PathVariable Long likeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		itemLikeService.deleteLikeOnItem(likeId, userDetails);
		return ResponseEntity.ok("삭제되었습니다.");
	}
}

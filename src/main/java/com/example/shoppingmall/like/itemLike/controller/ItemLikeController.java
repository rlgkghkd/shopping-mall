package com.example.shoppingmall.like.itemLike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.like.itemLike.dto.LeaveItemLikeResponseDto;
import com.example.shoppingmall.like.itemLike.service.ItemLikeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemLikeController {
	private final ItemLikeService itemLikeService;

	@PostMapping("/items/{itemId}/likes")
	public ResponseEntity<LeaveItemLikeResponseDto> leaveLikeOnItem(@PathVariable Long itemId,
		HttpServletRequest request) {
		return ResponseEntity.ok(itemLikeService.leaveLikeOnItem(itemId, request));
	}

	@DeleteMapping("/items/{itemId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnItem(@PathVariable Long likeId, HttpServletRequest request) {
		itemLikeService.deleteLikeOnItem(likeId, request);
		return ResponseEntity.ok("삭제되었습니다.");
	}
}

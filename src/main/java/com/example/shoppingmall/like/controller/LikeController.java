package com.example.shoppingmall.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.like.dto.LeaveLikeResponseDto;
import com.example.shoppingmall.like.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
	private final LikeService likeService;

	@PostMapping("/items/{itemId}/likes")
	public ResponseEntity<LeaveLikeResponseDto> leaveLikeOnItem(@PathVariable Long itemId) {
		return ResponseEntity.ok(likeService.leaveLikeOnItem(itemId, 1L));
	}

	@PostMapping("/comments/{commentId}/likes")
	public ResponseEntity<LeaveLikeResponseDto> leaveLikeOnComment(@PathVariable Long commentId) {
		return ResponseEntity.ok(likeService.leaveLikeOnComment(commentId, 1L));
	}

	@DeleteMapping("/items/{itemId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnItem(@PathVariable Long likeId) {
		likeService.deleteLikeOnItem(likeId, 1L);
		return ResponseEntity.ok("삭제되었습니다.");
	}

	@DeleteMapping("/comments/{commentId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnComment(@PathVariable Long likeId) {
		likeService.deleteLikeOnComment(likeId, 1L);
		return ResponseEntity.ok("삭제되었습니다.");
	}
}

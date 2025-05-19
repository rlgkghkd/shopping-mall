package com.example.shoppingmall.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.like.dto.LeaveLikeResponseDto;
import com.example.shoppingmall.like.service.LikeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
	private final LikeService likeService;

	@PostMapping("/items/{itemId}/likes")
	public ResponseEntity<LeaveLikeResponseDto> leaveLikeOnItem(@PathVariable Long itemId,
		HttpServletRequest request) {
		return ResponseEntity.ok(likeService.leaveLikeOnItem(itemId, request));
	}

	@PostMapping("/comments/{commentId}/likes")
	public ResponseEntity<LeaveLikeResponseDto> leaveLikeOnComment(@PathVariable Long commentId,
		HttpServletRequest request) {
		return ResponseEntity.ok(likeService.leaveLikeOnComment(commentId, request));
	}

	@DeleteMapping("/items/{itemId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnItem(@PathVariable Long likeId, HttpServletRequest request) {
		likeService.deleteLikeOnItem(likeId, request);
		return ResponseEntity.ok("삭제되었습니다.");
	}

	@DeleteMapping("/comments/{commentId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnComment(@PathVariable Long likeId, HttpServletRequest request) {
		likeService.deleteLikeOnComment(likeId, request);
		return ResponseEntity.ok("삭제되었습니다.");
	}
}

package com.example.shoppingmall.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.like.dto.LeaveLikeResponseDto;
import com.example.shoppingmall.like.service.LikeService;

import lombok.RequiredArgsConstructor;

@Controller
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

	//comments/{commentId}/likes/{likeId}
	//items/{itemId}/likes/{likeId} 아니면 이렇게 분리?
	@DeleteMapping("/likes/{likeId}")
	public ResponseEntity<String> deleteLike(@PathVariable Long likeId) {
		likeService.deleteLike(likeId, 1L);
		return ResponseEntity.ok("삭제되었습니다.");
	}
}

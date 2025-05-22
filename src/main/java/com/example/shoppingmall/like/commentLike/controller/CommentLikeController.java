package com.example.shoppingmall.like.commentLike.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.like.commentLike.dto.LeaveCommentLikeResponseDto;
import com.example.shoppingmall.like.commentLike.service.CommentLikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {
	private final CommentLikeService commentLikeService;

	@PostMapping("/comment/{commentId}/likes")
	public ResponseEntity<LeaveCommentLikeResponseDto> leaveLikeOnComment(@PathVariable Long commentId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(commentLikeService.leaveLikeOnComment(commentId, userDetails));
	}

	@DeleteMapping("/comment/{commentId}/likes/{likeId}")
	public ResponseEntity<String> deleteLikeOnComment(@PathVariable Long likeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		commentLikeService.deleteLikeOnComment(likeId, userDetails);
		return ResponseEntity.ok("삭제되었습니다.");
	}
}

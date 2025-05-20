package com.example.shoppingmall.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.comment.dto.request.CreateCommentRequestDto;
import com.example.shoppingmall.comment.dto.request.UpdateCommentRequestDto;
import com.example.shoppingmall.comment.dto.response.CreateCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.DeleteCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.FindByAllCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.FindByIdCommentResponseDto;
import com.example.shoppingmall.comment.dto.response.UpdateCommentResponseDto;
import com.example.shoppingmall.comment.service.CommentService;
import com.example.shoppingmall.common.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CreateCommentResponseDto> createdComment(
		@Valid @RequestBody CreateCommentRequestDto createCommentRequestDto,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		CreateCommentResponseDto createComment = commentService.createComment(createCommentRequestDto,
			customUserDetails);
		return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
	}

	@GetMapping
	public ResponseEntity<List<FindByAllCommentResponseDto>> findByAllComment() {
		List<FindByAllCommentResponseDto> findByAllCommentList = commentService.findByAllComment();
		return ResponseEntity.ok(findByAllCommentList);
	}

	@GetMapping("/{id}")
	public ResponseEntity<FindByIdCommentResponseDto> findByComment(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		FindByIdCommentResponseDto findByComment = commentService.findByComment(id, customUserDetails);
		return ResponseEntity.ok(findByComment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UpdateCommentResponseDto> updatedComment(
		@PathVariable Long id,
		@Valid @RequestBody UpdateCommentRequestDto updateCommentRequestDto,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		UpdateCommentResponseDto updateComment = commentService.updateComment(
			id,
			updateCommentRequestDto,
			customUserDetails
		);
		return ResponseEntity.ok(updateComment);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<DeleteCommentResponseDto> deletedComment(
		@PathVariable Long id,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		DeleteCommentResponseDto deleteComment = commentService.deleteComment(id, customUserDetails);
		return ResponseEntity.ok(deleteComment);
	}

}

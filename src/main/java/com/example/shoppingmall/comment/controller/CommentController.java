package com.example.shoppingmall.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.comment.dto.request.CreateCommentRequestDto;
import com.example.shoppingmall.comment.dto.response.CreateCommentResponseDto;
import com.example.shoppingmall.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CreateCommentResponseDto> createdComment(
		@Valid @RequestBody CreateCommentRequestDto createCommentRequestDto
	) {
		CreateCommentResponseDto createComment = commentService.createComment(createCommentRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createComment);
	}
}

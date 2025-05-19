package com.example.shoppingmall.comment.dto.response;

import lombok.Getter;

@Getter
public class DeleteCommentResponseDto {
	private String message;

	public DeleteCommentResponseDto(String message) {
		this.message = message;
	}
}

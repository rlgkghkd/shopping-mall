package com.example.shoppingmall.comment.dto.request;

import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
	private Long orderId;
	private Long commentId;
	private String content;
}

package com.example.shoppingmall.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class UpdateCommentResponseDto {
	private Long id;
	private Long orderId;
	private Long writer; // 추후에 유저 이름으로 바꿀 예정
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH-mm-ss")
	private LocalDateTime createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH-mm-ss")
	private LocalDateTime updatedAt;

	public UpdateCommentResponseDto(
		Long id,
		Long orderId,
		Long writer,  // 추후에 유저 이름으로 바꿀 예정
		String content,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		this.id = id;
		this.orderId = orderId;
		this.writer = writer;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}

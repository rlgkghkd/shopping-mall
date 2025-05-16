package com.example.shoppingmall.like.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveLikeResponseDto {
	private String likedContent;
	private Long likedContentId;
	private Long likeId;
	private LocalDateTime createdAt;
}

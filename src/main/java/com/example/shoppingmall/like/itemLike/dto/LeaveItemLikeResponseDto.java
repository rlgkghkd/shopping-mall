package com.example.shoppingmall.like.itemLike.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveItemLikeResponseDto {
	private Long likedContentId;
	private Long likeId;
	private LocalDateTime createdAt;
}

package com.example.shoppingmall.like.commentLike.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveCommentLikeResponseDto {
	private Long likedCommentId;
	private Long likeId;
	private LocalDateTime createdAt;
}

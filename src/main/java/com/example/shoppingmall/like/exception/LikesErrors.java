package com.example.shoppingmall.like.exception;

import com.example.shoppingmall.common.exception.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LikesErrors implements Errors {

	ALREADY_LIKED(400, "L001", "이미 좋아요 했습니다."),
	OTHER_USERS_LIKE(403, "L002", "본인이 남긴 좋아요가 아닙니다."),
	NOT_FOUND_LIKE(404, "L003", "좋아요가 존재하지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}

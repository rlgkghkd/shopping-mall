package com.example.shoppingmall.like.exception;

import com.example.shoppingmall.common.exception.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LikesErrors implements Errors {

	AlreadyLiked(400, "L001", "이미 좋아요 했습니다.");

	private final int status;
	private final String code;
	private final String message;

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}

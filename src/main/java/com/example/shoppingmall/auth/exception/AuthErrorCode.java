package com.example.shoppingmall.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

	EMAIL_ALREADY_EXISTS(404, "A001", "이미 존재하는 이메일입니다."),
	LOGIN_FAILED(404, "A002", "로그인에 실패했습니다."),
	INVALID_TOKEN(404, "A003", "유효한 토큰이 없습니다.");

	private final int status;
	private final String code;
	private final String message;
}

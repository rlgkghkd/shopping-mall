package com.example.shoppingmall.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

	NOT_FOUND_USER(404, "U001", "사용자 정보가 없습니다."),
	NO_ACCESS_PERMISSION(404, "U002", "접근권한이 없습니다."),
	PASSWORD_MISMATCH(404, "U003", "비밀번호가 일치하지 않습니다.");

	private final int status;
	private final String code;
	private final String message;
}

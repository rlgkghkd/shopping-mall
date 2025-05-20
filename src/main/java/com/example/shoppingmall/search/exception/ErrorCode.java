package com.example.shoppingmall.search.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	NOT_FOUND_INFORMATION(HttpStatus.NOT_FOUND,"404","정보를 찾을 수 없습니다. can not fount information");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

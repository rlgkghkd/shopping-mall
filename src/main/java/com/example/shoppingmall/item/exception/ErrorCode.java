package com.example.shoppingmall.item.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	NOT_FOUND_ITEM(HttpStatus.NOT_FOUND,"404","상품을 찾을 수 없습니다. can not fount item");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

package com.example.shoppingmall.search.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchErrorCode {

	NOT_FOUND_INFORMATION(404,"S001","정보를 찾을 수 없습니다.");

	private final int Status;
	private final String code;
	private final String message;
}

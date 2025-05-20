package com.example.shoppingmall.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
	private final Errors errors;

	public CustomException(Errors errors) {
		super(errors.getMessage());
		this.errors = errors;
	}
}

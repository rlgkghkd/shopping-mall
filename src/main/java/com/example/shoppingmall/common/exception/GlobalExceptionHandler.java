package com.example.shoppingmall.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponseDto> noTokenException(CustomException e) {
		Errors errors = e.getErrors();
		ErrorResponseDto response = ErrorResponseDto.of(errors, e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.valueOf(e.getErrors().getStatus()));
	}
}

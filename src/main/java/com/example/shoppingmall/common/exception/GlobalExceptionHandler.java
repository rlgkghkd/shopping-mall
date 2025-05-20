package com.example.shoppingmall.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.shoppingmall.like.exception.AlreadyLikedException;
import com.example.shoppingmall.like.exception.NoLikeFoundException;
import com.example.shoppingmall.like.exception.NoTokenException;
import com.example.shoppingmall.like.exception.WrongUsersLikeException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	//likes
	@ExceptionHandler(NoTokenException.class)
	public ResponseEntity<Map<String, Object>> noTokenException(NoTokenException e) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return createErrorResponse(status, e.getMessage());
	}

	@ExceptionHandler(AlreadyLikedException.class)
	public ResponseEntity<Map<String, Object>> alreadyLikedException(NoTokenException e) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return createErrorResponse(status, e.getMessage());
	}

	@ExceptionHandler(NoLikeFoundException.class)
	public ResponseEntity<Map<String, Object>> noLikeFoundException(NoTokenException e) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		return createErrorResponse(status, e.getMessage());
	}

	@ExceptionHandler(WrongUsersLikeException.class)
	public ResponseEntity<Map<String, Object>> wrongUsersLikeException(NoTokenException e) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		return createErrorResponse(status, e.getMessage());
	}

	//error response
	public ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", status.name());
		errorResponse.put("code", status.value());
		errorResponse.put("message", message);

		return new ResponseEntity<>(errorResponse, status);
	}
}

package com.example.shoppingmall.common.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)// null 필드는 JSON 응답에 포함되지 않음
public class ErrorResponseDto {

	private final LocalDateTime timestamp = LocalDateTime.now();
	private final int status;
	private final String code;
	private final String message;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<FieldError> fieldErrors;

	//일반적인 에러 응답을 생성
	public static ErrorResponseDto of(Errors errors) {
		return ErrorResponseDto.builder()
			.status(errors.getStatus())
			.code(errors.getCode())
			.message(errors.getMessage())
			.fieldErrors(new ArrayList<>())// 기본적으로 빈 리스트
			.build();
	}

	//에러 메시지를 커스터마이징하여 에러 응답을 생성
	public static ErrorResponseDto of(Errors errors, String message) {
		return ErrorResponseDto.builder()
			.status(errors.getStatus())
			.code(errors.getCode())
			.message(message)
			.fieldErrors(new ArrayList<>())
			.build();
	}

	//필드 유효성 에러를 포함한 에러 응답 생성
	public static ErrorResponseDto of(Errors errors, List<FieldError> fieldErrors) {
		return ErrorResponseDto.builder()
			.status(errors.getStatus())
			.code(errors.getCode())
			.message(errors.getMessage())
			.fieldErrors(fieldErrors)
			.build();
	}

	@Getter
	@Builder
	//개별 필드 에러 정보를 담는 내부 static 클래스
	public static class FieldError {
		private String field; // 문제가 발생한 필드명
		private String value; // 입력된 잘못된 값
		private String reason; // 왜 잘못되었는지에 대한 설명

		//FieldError 생성 메서드
		public static FieldError of(String field, String value, String reason) {
			return FieldError.builder()
				.field(field)
				.value(value)
				.reason(reason)
				.build();
		}
	}
}

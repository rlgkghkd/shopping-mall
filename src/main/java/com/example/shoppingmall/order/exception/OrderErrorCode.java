package com.example.shoppingmall.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode {

	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORD01", "존재하지 않는 주문입니다."),
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ORD02", "해당 상품을 찾을 수 없습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORD03", "해당 유저를 찾을 수 없습니다."),
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "ORD04", "로그인이 필요합니다."),
	FORBIDDEN_ORDER_ACCESS(HttpStatus.FORBIDDEN, "ORD05", "본인의 주문에만 접근할 수 있습니다."),
	FORBIDDEN_ORDER_LIST_ACCESS(HttpStatus.FORBIDDEN, "ORD06", "관리자만 전체 주문을 조회할 수 있습니다."),
	FORBIDDEN_ORDER_STATUS_UPDATE(HttpStatus.FORBIDDEN, "ORD07", "일반 사용자는 주문 상태를 변경할 수 없습니다."),
	INVALID_CANCEL_STATUS_USER(HttpStatus.BAD_REQUEST, "ORD08", "일반 사용자는 PENDING 상태에서만 주문을 취소할 수 있습니다."),
	INVALID_CANCEL_STATUS_ADMIN(HttpStatus.BAD_REQUEST, "ORD09", "관리자는 PROCESSING 이전까지만 주문을 취소할 수 있습니다."),
	ORDER_STATUS_REQUIRED(HttpStatus.BAD_REQUEST, "ORD010", "주문 상태는 필수입니다.");


	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}

package com.example.shoppingmall.order.exception;

import com.example.shoppingmall.common.exception.Errors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderErrorCode implements Errors {

	ORDER_NOT_FOUND(404, "ORD01", "존재하지 않는 주문입니다."),
	ITEM_NOT_FOUND(404, "ORD02", "해당 상품을 찾을 수 없습니다."),
	USER_NOT_FOUND(404, "ORD03", "해당 유저를 찾을 수 없습니다."),
	UNAUTHORIZED_USER(401, "ORD04", "로그인이 필요합니다."),
	FORBIDDEN_ORDER_ACCESS(403, "ORD05", "본인의 주문에만 접근할 수 있습니다."),
	FORBIDDEN_ORDER_LIST_ACCESS(403, "ORD06", "관리자만 전체 주문을 조회할 수 있습니다."),
	FORBIDDEN_ORDER_STATUS_UPDATE(403, "ORD07", "일반 사용자는 주문 상태를 변경할 수 없습니다."),
	INVALID_CANCEL_STATUS_USER(400, "ORD08", "일반 사용자는 PENDING 상태에서만 주문을 취소할 수 있습니다."),
	INVALID_CANCEL_STATUS_ADMIN(400, "ORD09", "관리자는 PROCESSING 이전까지만 주문을 취소할 수 있습니다."),
	ALREADY_CANCELED(400, "ORD10", "이미 취소된 주문입니다."),
	ORDER_STATUS_REQUIRED(400, "ORD11", "주문 상태는 필수입니다.");

	private final int status;
	private final String code;
	private final String message;

}

package com.example.shoppingmall.order.type;

public enum OrderStatus {
	PENDING,        // 주문 접수
	PROCESSING,     // 상품 준비 중
	SHIPPED,        // 배송 시작
	IN_TRANSIT,     // 배송 중
	DELIVERED,      // 배송 완료
	CANCELED        // 주문 취소
}

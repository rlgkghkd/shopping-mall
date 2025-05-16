package com.example.shoppingmall.order.dto;

import com.example.shoppingmall.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드는 JSON 응답에서 제외
public class OrderResponseDto {

	private Long orderId;
	private Long itemId;
	private String orderStatus;
	private String orderAddress;
	private Integer orderPrice;
	private LocalDateTime createdAt;
	private String message;

	public OrderResponseDto(Order order) {
		this.orderId = order.getId();
		this.itemId = order.getItem().getId();
		this.orderStatus = order.getOrderStatus().name();
		this.orderAddress = order.getOrderAddress();
		this.orderPrice = order.getOrderPrice();
		this.createdAt = order.getCreatedAt();

	}

	public OrderResponseDto(Order order, String message) {
		this(order);
		this.message = message;
	}
}

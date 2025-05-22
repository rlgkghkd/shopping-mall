package com.example.shoppingmall.order.dto;

import com.example.shoppingmall.order.type.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PutOrderRequestDto {


	@NotNull(message = "주문 상태는 필수입니다.")
	private OrderStatus orderStatus;

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
}

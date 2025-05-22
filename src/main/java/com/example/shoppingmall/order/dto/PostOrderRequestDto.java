package com.example.shoppingmall.order.dto;

import com.example.shoppingmall.order.type.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostOrderRequestDto {
	private Long itemId;
	private String orderAddress;
	private OrderStatus orderStatus;
	private Integer orderPrice;
}

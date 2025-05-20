package com.example.shoppingmall.order.entity;

import com.example.shoppingmall.common.BaseEntity;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.order.type.OrderStatus;
import com.example.shoppingmall.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	private String orderAddress;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private Integer orderPrice;


	public Order(User user, Item item, String orderAddress, OrderStatus status, Integer price) {
		this.user = user;
		this.item = item;
		this.orderAddress = orderAddress;
		this.orderStatus = status;
		this.orderPrice = price;
	}


	public void updateStatus(OrderStatus status) {
		this.orderStatus = status;
	}
}

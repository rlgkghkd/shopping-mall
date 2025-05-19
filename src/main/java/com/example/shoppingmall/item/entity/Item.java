package com.example.shoppingmall.item.entity;

import com.example.shoppingmall.common.BaseEntity;
import com.example.shoppingmall.item.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Item extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long Id;

	@Column(nullable = false)
	private String itemName;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private int price;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Setter
	private Long likeCount;

	public void update(String itemName, String content, int price, Category category) {
		this.itemName = itemName;
		this.content = content;
		this.price = price;
		this.category = category;

	}
}

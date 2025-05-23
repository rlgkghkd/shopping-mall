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
	private Long id;

	@Column(nullable = false)
	private String itemName;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private int price;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	@Column(columnDefinition = "bigint default 0")
	private Long likeCount;

	public void increaseLikeCount(Long amount) {
		this.likeCount += amount;
	}

	public void decreaseLikeCount(Long amount) {
		this.likeCount -= amount;
	}

	public void update(String itemName, String content, int price, Category category) {
		this.itemName = itemName;
		this.content = content;
		this.price = price;
		this.category = category;

	}

	// 테스트용 setter 추가
	public void setId(Long id) {
		this.id = id;
	}
}

package com.example.shoppingmall.item.dto;

import java.io.Serializable;

import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
// Serializable 직렬화
public class ItemResponseDto implements Serializable {

	private static final long serialVersionUID = 1L; // 직렬화 UID

	private long id;
	private String itemName;
	private String content;
	private int price;
	private Category category;

	public static ItemResponseDto fromEntity(Item item) {
		return ItemResponseDto.builder()
			.id(item.getId())
			.itemName(item.getItemName())
			.content(item.getContent())
			.price(item.getPrice())
			.category(item.getCategory())
			.build();
	}

}

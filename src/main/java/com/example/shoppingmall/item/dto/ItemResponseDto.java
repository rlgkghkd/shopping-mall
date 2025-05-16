package com.example.shoppingmall.item.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.shoppingmall.item.Category;
import com.example.shoppingmall.item.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemResponseDto {
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

	public static List<ItemResponseDto> fromEntityList(List<Item> items) {
		return items.stream()
			.map(ItemResponseDto::fromEntity)
			.collect(Collectors.toList());
	}

}

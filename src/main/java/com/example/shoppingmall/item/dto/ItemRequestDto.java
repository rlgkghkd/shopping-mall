package com.example.shoppingmall.item.dto;

import com.example.shoppingmall.item.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
	private String itemName;
	private String content;
	private int price;
	private Category category;

}

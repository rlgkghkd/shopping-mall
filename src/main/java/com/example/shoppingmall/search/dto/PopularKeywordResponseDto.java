package com.example.shoppingmall.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularKeywordResponseDto {
	private String keyword;
	private double score;
}

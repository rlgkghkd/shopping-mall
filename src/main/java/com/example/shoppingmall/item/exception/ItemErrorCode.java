package com.example.shoppingmall.item.exception;

import org.springframework.http.HttpStatus;

import com.example.shoppingmall.common.exception.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemErrorCode implements Errors {

	NOT_FOUND_ITEM(404,"I001","상품을 찾을 수 없습니다."),
	INVALID_ITEM_NAME(404, "I002", "상품명은 필수 입력값입니다."),
	INVALID_ITEM_PRICE(400,"I003","상품 가격은 0원 이상이어야 합니다."),
	INVALID_ITEM_CATEGORY(400,"I004","유효하지 않은 상품 카테고리입니다."),
	DUPLICATE_ITEM_NAME(400, "I005", "이미 동일한 상품명이 존재합니다.");

	private final int Status;
	private final String code;
	private final String message;
}

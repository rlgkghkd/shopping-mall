package com.example.shoppingmall.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{
	private final JPAQueryFactory queryFactory;
}

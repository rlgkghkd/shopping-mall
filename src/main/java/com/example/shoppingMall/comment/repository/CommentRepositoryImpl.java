package com.example.shoppingMall.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
	private final JPAQueryFactory queryFactory;
}

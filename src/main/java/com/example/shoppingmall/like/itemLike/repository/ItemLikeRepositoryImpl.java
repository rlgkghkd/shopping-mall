package com.example.shoppingmall.like.itemLike.repository;

import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.entity.QItem;
import com.example.shoppingmall.like.itemLike.entity.QItemLike;
import com.example.shoppingmall.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemLikeRepositoryImpl implements ItemLikeRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public boolean searchLikeByUserAndItem(Long userId, Item item) {

		QItemLike like = QItemLike.itemLike;
		QUser user = QUser.user;
		QItem qItem = QItem.item;
		System.out.println("k where");
		return queryFactory.select(like.id) // id만 조회하여 존재 여부 체크
			.from(like)
			.where(
				user.id.eq(userId),
				item != null ? qItem.Id.eq(item.getId()) : null
			).fetchFirst() != null;
	}
}

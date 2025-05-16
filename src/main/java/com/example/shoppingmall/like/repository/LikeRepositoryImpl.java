package com.example.shoppingmall.like.repository;

import com.example.shoppingmall.comment.entity.QComment;
import com.example.shoppingmall.item.entity.QItem;
import com.example.shoppingmall.like.entity.QLike;
import com.example.shoppingmall.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public boolean searchLikeByUserAndContent(Long userId, Long commentId, Long itemId) {
		QLike like = QLike.like;
		QUser user = QUser.user;
		QComment comment = QComment.comment;
		QItem item = QItem.item;
		return queryFactory.selectOne()
			.from(like)
			.where(
				user.id.eq(userId),
				comment != null ? comment.id.eq(commentId) : null,
				itemId != null ? item.id.eq(itemId) : null
			).fetch() != null;
	}
}

package com.example.shoppingmall.like.repository;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.entity.QComment;
import com.example.shoppingmall.item.entity.Item;
import com.example.shoppingmall.item.entity.QItem;
import com.example.shoppingmall.like.entity.QLike;
import com.example.shoppingmall.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public boolean searchLikeByUserAndContent(Long userId, Comment comment, Item item) {
		QLike like = QLike.like;
		QUser user = QUser.user;
		QComment qComment = QComment.comment;
		QItem qItem = QItem.item;
		return queryFactory.selectOne()
			.from(like)
			.where(
				user.id.eq(userId),
				comment != null ? qComment.eq(comment) : null,
				item != null ? qItem.eq(item) : null
			).fetch() != null;
	}
}

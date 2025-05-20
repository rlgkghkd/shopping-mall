package com.example.shoppingmall.like.commentLike.repository;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.comment.entity.QComment;
import com.example.shoppingmall.like.commentLike.entity.QCommentLike;
import com.example.shoppingmall.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public boolean searchLikeByUserAndComment(Long userId, Comment comment) {

		QCommentLike commentLike = QCommentLike.commentLike;
		QUser user = QUser.user;
		QComment qComment = QComment.comment;
		System.out.println("k where");
		return queryFactory.select(commentLike.id) // id만 조회하여 존재 여부 체크
			.from(commentLike)
			.where(
				user.id.eq(userId),
				comment != null ? qComment.id.eq(comment.getId()) : null
			).fetchFirst() != null;
	}
}

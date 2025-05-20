package com.example.shoppingmall.like.commentLike.repository;

import com.example.shoppingmall.comment.entity.Comment;

public interface CommentLikeRepositoryCustom {
	boolean searchLikeByUserAndComment(Long userId, Comment comment);
}

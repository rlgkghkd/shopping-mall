package com.example.shoppingmall.like.repository;

import com.example.shoppingmall.comment.entity.Comment;
import com.example.shoppingmall.item.entity.Item;

public interface LikeRepositoryCustom {
	boolean searchLikeByUserAndContent(Long userId, Comment comment, Item item);
}

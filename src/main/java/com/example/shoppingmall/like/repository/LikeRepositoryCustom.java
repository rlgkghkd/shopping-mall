package com.example.shoppingmall.like.repository;

public interface LikeRepositoryCustom {
	boolean searchLikeByUserAndContent(Long userId, Long commentId, Long itemId);
}

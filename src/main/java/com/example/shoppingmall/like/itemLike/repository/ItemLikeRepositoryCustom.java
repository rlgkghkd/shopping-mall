package com.example.shoppingmall.like.itemLike.repository;

import com.example.shoppingmall.item.entity.Item;

public interface ItemLikeRepositoryCustom {
	boolean searchLikeByUserAndItem(Long userId, Item item);
}

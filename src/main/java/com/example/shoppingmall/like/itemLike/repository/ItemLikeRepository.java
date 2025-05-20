package com.example.shoppingmall.like.itemLike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoppingmall.like.itemLike.entity.ItemLike;

@Repository
public interface ItemLikeRepository extends JpaRepository<ItemLike, Long>, ItemLikeRepositoryCustom {
}

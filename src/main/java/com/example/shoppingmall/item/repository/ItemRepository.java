package com.example.shoppingmall.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.example.shoppingmall.item.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom{

	@Query("SELECT i FROM Item i WHERE i.itemName LIKE %:keyword%")
	Page<Item> findByItemNameContaining(@Param("keyword") String keyword, Pageable pageable);

	boolean existsByItemName(String itemName);
}

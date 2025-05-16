package com.example.shoppingmall.item.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
}

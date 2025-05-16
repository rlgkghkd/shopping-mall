package com.example.shoppingMall.item.service;

import org.springframework.stereotype.Service;

import com.example.shoppingMall.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
}

package com.example.shoppingMall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingMall.item.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;
}

package com.example.shoppingmall.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.item.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;
}

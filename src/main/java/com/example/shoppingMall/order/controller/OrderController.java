package com.example.shoppingMall.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingMall.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
}

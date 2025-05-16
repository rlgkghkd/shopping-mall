package com.example.shoppingMall.order.service;

import org.springframework.stereotype.Service;

import com.example.shoppingMall.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
}

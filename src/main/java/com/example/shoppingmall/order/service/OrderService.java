package com.example.shoppingmall.order.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
}

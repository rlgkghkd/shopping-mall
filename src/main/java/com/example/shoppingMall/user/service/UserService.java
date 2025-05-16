package com.example.shoppingMall.user.service;

import org.springframework.stereotype.Service;

import com.example.shoppingMall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
}

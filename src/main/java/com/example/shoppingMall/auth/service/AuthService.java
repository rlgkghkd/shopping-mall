package com.example.shoppingMall.auth.service;

import org.springframework.stereotype.Service;

import com.example.shoppingMall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
}

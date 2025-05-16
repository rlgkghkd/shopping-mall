package com.example.shoppingmall.auth.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
}

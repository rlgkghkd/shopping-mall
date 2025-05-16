package com.example.shoppingmall.user.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
}

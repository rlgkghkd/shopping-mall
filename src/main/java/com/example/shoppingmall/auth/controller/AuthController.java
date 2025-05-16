package com.example.shoppingmall.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
}

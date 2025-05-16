package com.example.shoppingmall.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
}

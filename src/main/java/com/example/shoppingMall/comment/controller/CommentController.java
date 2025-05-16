package com.example.shoppingMall.comment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingMall.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
}

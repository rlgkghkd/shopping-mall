package com.example.shoppingmall.comment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
}

package com.example.shoppingmall.comment.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
}

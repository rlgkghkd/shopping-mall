package com.example.shoppingMall.comment.service;

import org.springframework.stereotype.Service;

import com.example.shoppingMall.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
}

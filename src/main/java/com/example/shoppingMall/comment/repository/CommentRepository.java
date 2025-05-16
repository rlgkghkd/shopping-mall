package com.example.shoppingMall.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoppingMall.comment.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> , CommentRepositoryCustom{
}

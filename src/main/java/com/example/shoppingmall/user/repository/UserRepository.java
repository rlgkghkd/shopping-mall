package com.example.shoppingmall.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoppingmall.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}

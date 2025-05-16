package com.example.shoppingmall.user.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
	private Long userId;
	private String userName;
	private String userRole;
	private LocalDateTime createdAt;
}

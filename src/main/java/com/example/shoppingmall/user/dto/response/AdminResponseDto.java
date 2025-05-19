package com.example.shoppingmall.user.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminResponseDto {
	private Long userId;
	private String userName;
	private String email;
	private String userRole;
	private LocalDateTime createdAt;
}

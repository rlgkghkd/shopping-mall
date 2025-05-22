package com.example.shoppingmall.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePasswordRequestDto {
	private String oldPassword;
	private String newPassword;
}

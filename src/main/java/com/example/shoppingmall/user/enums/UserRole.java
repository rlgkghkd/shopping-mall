package com.example.shoppingmall.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
	USER(0, "회원"),
	ADMIN(1, "관리자");

	private final int code;
	private final String description;

	public static UserRole of(Integer dbData) { //아무생각없이 private로 해서 오류남
		/*
		dbData 값이 enum에 있는 값과 일치하는지 확인해서 그 값을 반환함
		value() : enum값들 전부 => enum의 모든 상수를 하나씩 꺼내서 검사할래 그 값을 userRole에 넣을거야 의미
		 */
		for (UserRole userRole : values()) {
			if (userRole.getCode() == (dbData)) {
				return userRole;
			}
		}

		throw new IllegalArgumentException("Invalid code :" + dbData);
	}

}

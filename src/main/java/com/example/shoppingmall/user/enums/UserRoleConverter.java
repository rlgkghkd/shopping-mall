package com.example.shoppingmall.user.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {
	/*
    AttributeConverter : JPA에서 엔티티의 필드 값과 DB값 간의 변환을 담당하는 인터페이스이다.
   자박 객체 <-> 데이터베이스 값 간의 자동 변환을 도와준다.
   db에는 숫자를 저장하고, 자바에서는 userRole.ADMIN이런거 사용하고 싶을때 사용한다.
   */
	@Override
	public Integer convertToDatabaseColumn(UserRole userRole) { // 자바 객체 -> DB 저장
		return userRole.getCode();
	}

	@Override
	public UserRole convertToEntityAttribute(Integer dbData) { // DB에서 조회 -> 자바 객체
		return UserRole.of(dbData);
	}
}

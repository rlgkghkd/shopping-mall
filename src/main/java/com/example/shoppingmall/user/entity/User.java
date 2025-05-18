package com.example.shoppingmall.user.entity;

import java.time.LocalDateTime;

import com.example.shoppingmall.user.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	//@Enumerated(value = EnumType.STRING)
	//@Convert(converter = UserRoleConverter.class) => autoApply = ture가 자동으로 해줌
	private UserRole userRole;

	@Column
	private LocalDateTime createdAt;

	@Column
	private Boolean isDeleted = false;

	public User(String userName, String email, UserRole userRole, String encodePassword) {
		this.username = userName;
		this.email = email;
		this.userRole = userRole;
		this.password = encodePassword;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void softDeleted() {
		this.isDeleted = true;
	}
}

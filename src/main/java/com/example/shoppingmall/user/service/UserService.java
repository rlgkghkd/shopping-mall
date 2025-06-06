package com.example.shoppingmall.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.user.dto.request.DeleteRequestDto;
import com.example.shoppingmall.user.dto.request.UpdatePasswordRequestDto;
import com.example.shoppingmall.user.dto.response.AdminResponseDto;
import com.example.shoppingmall.user.dto.response.UserResponseDto;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.enums.UserRole;
import com.example.shoppingmall.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public List<AdminResponseDto> findAll(CustomUserDetails customUserDetails) {
		UserRole userRole = UserRole.valueOf(customUserDetails.getUserRole());
		//enum으로 꺼냄
		if (!userRole.equals(UserRole.ADMIN)) {
			throw new IllegalArgumentException("접근권한이 없습니다.");
		}
		List<User> userList = userRepository.findAll();

		return userList.stream()
			.map(user -> new AdminResponseDto(
					user.getId(),
					user.getUsername(),
					user.getEmail(),
					user.getUserRole().getDescription(),
					user.getCreatedAt()
				)
			)
			.toList();

	}

	public UserResponseDto findById(CustomUserDetails customUserDetails) {
		User user = verifyUser(customUserDetails.getUserId());

		return new UserResponseDto(
			user.getId(),
			user.getUsername(),
			user.getUserRole().getDescription(),
			user.getCreatedAt()
		);
	}

	@Transactional
	public void updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto, CustomUserDetails customUserDetails) {
		User user = verifyUser(customUserDetails.getUserId());

		if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}

		String encoderPassword = passwordEncoder.encode(updatePasswordRequestDto.getNewPassword());

		user.updatePassword(encoderPassword);

	}

	@Transactional
	public void delete(DeleteRequestDto deleteRequestDto, CustomUserDetails customUserDetails) {
		User user = verifyUser(customUserDetails.getUserId());

		if (!passwordEncoder.matches(deleteRequestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}
		user.softDeleted();

	}

	private User verifyUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다"));
	}

}

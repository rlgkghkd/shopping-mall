package com.example.shoppingmall.user.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.user.dto.request.DeleteRequestDto;
import com.example.shoppingmall.user.dto.request.UpdatePasswordRequestDto;
import com.example.shoppingmall.user.dto.response.UserResponseDto;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	/*
	JWT + security 넣으면 사용자 본인 확인 기능 추가
	 */

	public UserResponseDto findById(Long userId) {
		User user = verifyUser(userId);

		return new UserResponseDto(
			user.getId(),
			user.getUsername(),
			user.getUserRole().getDescription(),
			user.getCreatedAt()
		);
	}

	/*
	아직 JWt + security 사용안 해서 수동으로 비밀번호 대조함
	*/
	@Transactional
	public void updatePassword(Long userId, UpdatePasswordRequestDto updatePasswordRequestDto) {
		User user = verifyUser(userId);

		if (!user.getPassword().equals(updatePasswordRequestDto.getOldPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}
		user.updatePassword(updatePasswordRequestDto.getNewPassword());

	}

	@Transactional
	public void delete(Long userId, DeleteRequestDto deleteRequestDto) {
		User user = verifyUser(userId);

		if (!user.getPassword().equals(deleteRequestDto.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}
		user.softDeleted();

	}

	private User verifyUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다"));
	}

}

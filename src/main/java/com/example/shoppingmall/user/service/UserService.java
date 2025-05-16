package com.example.shoppingmall.user.service;

import org.springframework.stereotype.Service;

import com.example.shoppingmall.user.dto.request.DeleteRequestDto;
import com.example.shoppingmall.user.dto.request.UpdatePasswordRequestDto;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	/*
	이미 로그인을 끝내고 하는 로직이기에 userRole 필요없을 듯
	*/
	@Transactional
	public void updatePassword(Long userId, UpdatePasswordRequestDto updatePasswordRequestDto) {
		User user = verifyUser(userId);
		//암호화하면 암호화부분 추가

		if (!user.getPassword().equals(updatePasswordRequestDto.getOldPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
		}
		user.updatePassword(updatePasswordRequestDto.getNewPassword());

	}

	@Transactional
	public void delete(Long userId, DeleteRequestDto deleteRequestDto) {
		User user = verifyUser(userId);

		user.softDeleted();

	}

	private User verifyUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다"));
	}
}

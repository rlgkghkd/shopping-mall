package com.example.shoppingmall.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.user.dto.request.DeleteRequestDto;
import com.example.shoppingmall.user.dto.request.UpdatePasswordRequestDto;
import com.example.shoppingmall.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	/*
	회원 비밀번호 수정
	암호화 추가 예정
	 */
	@PatchMapping("/{userId}")
	public ResponseEntity<String> updatePassword(
		@PathVariable Long userId,
		@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto
	) {
		userService.updatePassword(userId, updatePasswordRequestDto);
		return ResponseEntity.ok("비밀번호 수정이 완료되었습니다");
	}

	/*
	회원탈퇴 : 비밀번호 입력으로 한번 더 확인
	암호화 추가 예정
	 */
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> delete(
		@PathVariable Long userId,
		@RequestBody DeleteRequestDto deleteRequestDto
	) {
		userService.delete(userId, deleteRequestDto);
		return ResponseEntity.ok("회원을 탈퇴했습니다");
	}
}

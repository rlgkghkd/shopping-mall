package com.example.shoppingmall.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.common.CustomUserDetails;
import com.example.shoppingmall.user.dto.request.DeleteRequestDto;
import com.example.shoppingmall.user.dto.request.UpdatePasswordRequestDto;
import com.example.shoppingmall.user.dto.response.AdminResponseDto;
import com.example.shoppingmall.user.dto.response.UserResponseDto;
import com.example.shoppingmall.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping()
	public ResponseEntity<List<AdminResponseDto>> findAll(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		List<AdminResponseDto> adminResponseDto = userService.findAll(customUserDetails);

		return new ResponseEntity<>(adminResponseDto, HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> findById(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		UserResponseDto userResponseDto = userService.findById(customUserDetails);
		return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}

	/*
	회원 비밀번호 수정
	 */
	@PatchMapping()
	public ResponseEntity<String> updatePassword(
		@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		userService.updatePassword(updatePasswordRequestDto, customUserDetails);
		return ResponseEntity.ok("비밀번호 수정이 완료되었습니다");
	}

	/*
	회원탈퇴
	 */
	@DeleteMapping()
	public ResponseEntity<String> delete(
		@RequestBody DeleteRequestDto deleteRequestDto,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		userService.delete(deleteRequestDto, customUserDetails);
		return ResponseEntity.ok("회원을 탈퇴했습니다");
	}
}

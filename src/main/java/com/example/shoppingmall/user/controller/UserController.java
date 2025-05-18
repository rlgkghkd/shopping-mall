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

import com.example.shoppingmall.user.dto.request.DeleteRequestDto;
import com.example.shoppingmall.user.dto.request.UpdatePasswordRequestDto;
import com.example.shoppingmall.user.dto.response.AdminResponseDto;
import com.example.shoppingmall.user.dto.response.UserResponseDto;
import com.example.shoppingmall.user.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping()
	public ResponseEntity<List<AdminResponseDto>> findAll(
		@AuthenticationPrincipal Claims claim
	) {
		List<AdminResponseDto> adminResponseDto = userService.findAll(claim);

		return new ResponseEntity<>(adminResponseDto, HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> findById(
		@AuthenticationPrincipal Claims claim
	) {
		UserResponseDto userResponseDto = userService.findById(claim);
		return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}

	/*
	회원 비밀번호 수정
	암호화 추가 예정
	 */
	@PatchMapping()
	public ResponseEntity<String> updatePassword(
		@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto,
		@AuthenticationPrincipal Claims claim
	) {
		userService.updatePassword(updatePasswordRequestDto, claim);
		return ResponseEntity.ok("비밀번호 수정이 완료되었습니다");
	}

	/*
	회원탈퇴 : 비밀번호 입력으로 한번 더 확인
	암호화 추가 예정
	 */
	@DeleteMapping()
	public ResponseEntity<String> delete(
		@RequestBody DeleteRequestDto deleteRequestDto,
		@AuthenticationPrincipal Claims claim
	) {
		userService.delete(deleteRequestDto, claim);
		return ResponseEntity.ok("회원을 탈퇴했습니다");
	}
}

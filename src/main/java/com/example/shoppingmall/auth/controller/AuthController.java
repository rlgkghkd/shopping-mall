package com.example.shoppingmall.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shoppingmall.auth.dto.request.LoginRequestDto;
import com.example.shoppingmall.auth.dto.request.SignUpRequestDto;
import com.example.shoppingmall.auth.dto.response.LoginResponseDto;
import com.example.shoppingmall.auth.dto.response.SignUpResponseDto;
import com.example.shoppingmall.auth.service.AuthService;
import com.example.shoppingmall.common.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signupRequestDto) {
		SignUpResponseDto signUpResponseDto = authService.signUp(signupRequestDto);
		return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		authService.logout(request);
		return ResponseEntity.ok("로그아웃 되었습니다.");
	}

}

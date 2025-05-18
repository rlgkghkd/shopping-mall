package com.example.shoppingmall.auth.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shoppingmall.auth.dto.request.LoginRequestDto;
import com.example.shoppingmall.auth.dto.request.SignUpRequestDto;
import com.example.shoppingmall.auth.dto.response.LoginResponseDto;
import com.example.shoppingmall.auth.dto.response.SignUpResponseDto;
import com.example.shoppingmall.common.JwtUtil;
import com.example.shoppingmall.user.entity.User;
import com.example.shoppingmall.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public SignUpResponseDto signUp(SignUpRequestDto signupRequestDto) {
		/*
		이미 존재하는 이메일인지 확인한번 하자
		 */
		if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		String encodePassword = passwordEncoder.encode(signupRequestDto.getPassword());

		User user = new User(
			signupRequestDto.getUserName(),
			signupRequestDto.getEmail(),
			signupRequestDto.getUserRole(),
			encodePassword);

		User saveUser = userRepository.save(user);
		return new SignUpResponseDto(
			saveUser.getId(),
			saveUser.getUsername(),
			saveUser.getEmail(),
			saveUser.getUserRole().getDescription(),
			saveUser.getCreatedAt()
		);
	}

	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		Optional<User> user = userRepository.findByEmail(loginRequestDto.getEmail()); //optional이 데이터 타입인가?

		if (user.isEmpty()) {
			throw new IllegalArgumentException("로그인에 실패했습니다.");
		}
		/*
		Optional<User> user = userRepository.findByEmail(...);
		user.get()    // User 객체를 꺼내는 것
		user.get().getPassword() // User 객체에서 비밀번호 꺼내는 것
		user가 Optional<User>라서 바로 getPassword() 호출 불가
		user.get()으로 Optional 안의 실제 User 객체를 꺼내서 사용해야 함
		 */
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) {
			throw new IllegalArgumentException("로그인에 실패했습니다.");
		}

		String token = jwtUtil.createToken(user.get().getId(), user.get().getUserRole());
		return new LoginResponseDto(token);
	}

	public void logout(HttpServletRequest request) {
		String token = jwtUtil.subStringToken(request);

		if (token == null) {
			throw new IllegalArgumentException("유효한 토큰이 없습니다.");
		}
		//레디스로 블랙리스트 관리

		if (jwtUtil.expiration(token) > 0) {
			redisTemplate.opsForValue()
				.set("blacklist:" + token, "logout", jwtUtil.expiration(token), TimeUnit.MILLISECONDS);
		}
	}
}

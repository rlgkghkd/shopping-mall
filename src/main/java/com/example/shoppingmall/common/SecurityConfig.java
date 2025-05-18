package com.example.shoppingmall.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
//@Slf4j 어노테이션을 왜 쓰는지 앞으로 생각하고 쓰자
@EnableWebSecurity //security 사용할 때 필요한 친구
@RequiredArgsConstructor
public class SecurityConfig {

	private final SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(
				auth -> auth
					.requestMatchers("/api/auth/**")
					.permitAll()
					.requestMatchers(HttpMethod.GET, "/api/users")
					.hasRole("ADMIN")
					.anyRequest()
					.authenticated()
			)
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); //이걸로 filter 등록함

		return http.build();
	}
	/*
	JWT + Security 구동 방식
	=> 처음에 로그인해서 들어오면 securityConfig에 와서 addFilterBefore에 적혀있는 securityFilter로 가서 token이 있는지 확인하고,
	있으면 파싱해서 claim을 얻는다 그리고나서
	UsernamePasswordAuthenticationFilter.class가 실행되고, 폼 로그인 요청을 처리한다.
	그리고 나서 이 securityConfig 클래스에 설정해 놓은 url을 허락할지 말지를 결정해서 못 들어가는
	role이면 에러를 반환하고 맞으면 controller로 들어간다.
	그리고 처음 회원가입하는 친구는 틀러와서 securityfilter로 가면 토큰 값이 없기 때문에
	그냥 filter를 뺘져 나와서 securityConfig로 오는데 이때 무조건 허락하는 url로 적어놓았기 때문에 controller로 들어갈 수 있다.

	=> 내용 다시 정리
	 */

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

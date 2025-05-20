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
					.requestMatchers("/api/orders/**").authenticated()
					.requestMatchers(HttpMethod.GET, "/api/users")
					.hasRole("ADMIN")
					.requestMatchers(HttpMethod.GET, "/api/users/me")
					.hasRole("USER")
					.anyRequest()
					.authenticated()
			)
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); //이걸로 filter 등록함

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

package com.example.shoppingmall.common;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
	/*
	http 요청마다 딱 한번만 실행되는 필터이다.
	 */

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// 헤더를 통해 들어온 토큰을 빼냄 => 이때 토큰은 string 형태임
		/*
		if (jwtUtil.subStringToken(authHeader))
		=> if문 안에 있는 값은 boolean 이다.
		 */
		//BEARER 떼어냄
		String uri = request.getRequestURI();
		if (uri.startsWith("/api/auth")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = jwtUtil.subStringToken(request);
		/*
		filterChian.doFilter(request, response)
		=>  요청을 다음으로 넘기는 것으로
		인증 성공이나 실패와 관련이 없다. -> 내가 할 일 없으면 그냥 넘기자라는 의미로 생각하면 된다.
		그래서 예외 상황에 사용할 때는 여기서는 실패했는데 혹시 다음 필터나 서블릿에서 할 거 있으면 해봐 할 때 사용함
		그래서 예외 상황에서 넘기는 건 인증이 필수가 아닐때 사용할 수 있다.
		 */
		/*
		토큰 값을 claims로 바꾸기

		 */
		try {
			if (token != null && jwtUtil.validationToken(token)) { //유효성 검사
				Claims claim = jwtUtil.extractClaim(token); // 토큰에 내가 넣은 값을 덩어리로 가져옴
				/*
				나는 사용자 정보를 내 JWT에 전부 저장하였기때문에 DB에서 조회하지 않고,
				userdetailsservice를 생략할 수 있다.
				 */
				/*
				JWT에 저장된 유저의 역할을 가져와서 저장하기 위한 코드 => 내가 이넘으로 만든 값을 String으로 바꿔서 저장함

				 */
				List<SimpleGrantedAuthority> authorities =
					List.of(new SimpleGrantedAuthority("ROLE_" + claim.get("userRole", String.class)));
				/*
				Authentication 객체의 한 구현체이다.
				현재 인증된 사용자 정보와 권한을 담는다.
				JWT에서 사용자 정보를 꺼낸 걸 spring security의 인증 객체로 바꾸기 위해 씀
				왜냐면 이 필터가 끝나고 securityConfig에 가서 인증을 할 수 있어서 그런가?
				이렇게 만들어진 authentication 구현체는 securityContextHolder.getContext().setAuthentication()에 저장해서 현제 요청이
				인증된 사용자임을 알려줌
				UsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities)
				principal : 사용자 인증 정보(userId) -> 이게 @AuthentictionPrincipal에 들어가는 내용
				credentials: 인증 수단 (JWT 인증에서는 null 가능)
				authorities : 사용자 권한 정보
				 */
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claim,
					null,
					authorities);

				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 접급입니다");
		}
		filterChain.doFilter(request, response);
	}

}


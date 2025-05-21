package com.example.shoppingmall.common;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.shoppingmall.user.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_TIME = 60 * 60 * 1000L;

	@Value("${jwt.secret}")
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	public String createToken(Long userId, UserRole userRole, String email) {
		Date date = new Date();

		return BEARER_PREFIX + Jwts.builder() //jwt을 이렇게 만들겠다 하는 매서드
			.setSubject(String.valueOf(userId))
			.claim("email", email)
			.claim("userRole", userRole) //enum이어도 자동으로 String으로 들어감
			.setExpiration(new Date(date.getTime() + TOKEN_TIME))
			.setIssuedAt(date)
			.signWith(key, signatureAlgorithm)
			.compact();
	}

	public String subStringToken(HttpServletRequest request) {
		String tokenValue = request.getHeader("Authorization");

		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(BEARER_PREFIX.length()); // 7
		}

		return null;
	}

	public Claims extractClaim(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public boolean validationToken(String token) {
		try {
			extractClaim(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long expiration(String token) {
		Claims claim = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
		/*
		JWT 토큰의 만료 시간까지 얼마나 남았는지 밀리초 단위로 계산함
		 */
		return claim.getExpiration().getTime() - System.currentTimeMillis();

	}

	public Long getUserIdFromToken(String token) {
		Claims claims = extractClaim(token);
		return Long.parseLong(claims.getSubject());
	}

	public UserRole getUserRoleFromToken(String token) {
		Claims claims = extractClaim(token);
		return UserRole.valueOf(claims.get("userRole", String.class));
	}

}

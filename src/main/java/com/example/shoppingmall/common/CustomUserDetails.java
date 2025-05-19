package com.example.shoppingmall.common;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final Long userId;
	private final String email;
	private final String userRole;
	private final List<GrantedAuthority> authorities;

	public CustomUserDetails(Long userId, String userRole, String email) {
		this.userId = userId;
		this.userRole = userRole;
		this.email = email;
		this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userRole));

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return email; //이거 안 하고 userId하고 싶으면 그냥 매소드 하나 만들어서 해도 됨
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}

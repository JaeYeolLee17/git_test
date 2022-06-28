package com.e4motion.challenge.data.provider.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String nickname;
	private String email;
	private String phone;

	public CustomUser(Long userId, String username, String password, String nickname, String email, String phone, Boolean enabled,
					  Collection<? extends GrantedAuthority> authorities) {

		super(username, password, enabled, true, true, true, authorities);
		this.userId = userId;
		this.nickname = nickname;
		this.email = email;
		this.phone = phone;
	}

}
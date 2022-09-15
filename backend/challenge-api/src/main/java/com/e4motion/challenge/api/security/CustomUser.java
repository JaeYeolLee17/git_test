package com.e4motion.challenge.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private final Long userId;
	private final String nickname;
	private final String email;
	private final String phone;
   	
   	public CustomUser(Long userId, String username, String password, String nickname, String email, String phone, Boolean disabled,
					  Collection<? extends GrantedAuthority> authorities) {

		super(username, password, disabled == null || !disabled, true, true, true, authorities);
		this.userId = userId;
		this.nickname = nickname;
   		this.email = email;
   		this.phone = phone;
   	}

}

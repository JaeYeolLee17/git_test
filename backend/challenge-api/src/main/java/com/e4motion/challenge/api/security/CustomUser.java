package com.e4motion.challenge.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private String customUsername;
	private String email;
	private String phone;
   	
   	public CustomUser(String userId, String password, String username, String email, String phone, Boolean enabled,
   			Collection<? extends GrantedAuthority> authorities) {

		super(userId, password, enabled, true, true, true, authorities);
   		this.customUsername = username;
   		this.email = email;
   		this.phone = phone;
   	}

}

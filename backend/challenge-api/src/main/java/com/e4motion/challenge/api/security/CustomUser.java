package com.e4motion.challenge.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private final String name;
	private final String email;
	private final String phone;
   	
   	public CustomUser(String username, String password, String name, String email, String phone, Boolean disabled,
					  Collection<? extends GrantedAuthority> authorities) {

		super(username, password, disabled == null || !disabled, true, true, true, authorities);
		this.name = name;
   		this.email = email;
   		this.phone = phone;
   	}

}

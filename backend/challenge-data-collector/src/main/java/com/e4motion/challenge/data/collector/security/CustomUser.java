package com.e4motion.challenge.data.collector.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private final boolean settingsUpdated;
   	
   	public CustomUser(String cameraNo, String password, boolean settingsUpdated,
					  Collection<? extends GrantedAuthority> authorities) {
   		
   		super(cameraNo, password, authorities);
   		this.settingsUpdated = settingsUpdated;
   	}

}

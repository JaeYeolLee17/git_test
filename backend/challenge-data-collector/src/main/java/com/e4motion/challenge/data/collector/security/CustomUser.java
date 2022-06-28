package com.e4motion.challenge.data.collector.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	private boolean settingsUpdated;
   	
   	public CustomUser(String cameraId, String password, boolean settingsUpdated,
					  Collection<? extends GrantedAuthority> authorities) {
   		
   		super(cameraId, password, authorities);
   		this.settingsUpdated = settingsUpdated;
   	}

}

package com.e4motion.challenge.api.dto;

import java.util.Set;

import com.e4motion.challenge.api.entity.Authority;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String userId;
    private String password;
    private String username;
    private String email;
    private Set<Authority> authorities;

    @Builder
    public UserDto(String userId, String username, String email, Set<Authority> authorities) {
    	this.userId = userId;
    	this.username = username;
    	this.email = email;
    	this.authorities = authorities;
    }
}

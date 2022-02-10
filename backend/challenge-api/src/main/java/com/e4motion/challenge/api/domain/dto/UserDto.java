package com.e4motion.challenge.api.domain.dto;

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
    private String authority;

    @Builder
    public UserDto(String userId, String username, String email, String authority) {
    	this.userId = userId;
    	this.username = username;
    	this.email = email;
    	this.authority = authority;
    }
}

package com.e4motion.challenge.api.dto;

import java.util.Set;

import com.e4motion.challenge.api.domain.Authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userId;
    private String password;
    private String username;
    private String email;
    private Set<Authority> authorities;

}

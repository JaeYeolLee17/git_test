package com.e4motion.challenge.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String userId;
    private String password;
    private String username;
    private String email;
    private String phone;
    private String authority;
    
}

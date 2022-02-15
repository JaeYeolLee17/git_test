package com.e4motion.challenge.data.provider.dto;

import com.e4motion.challenge.common.domain.AuthorityName;

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
    private AuthorityName authority;
    
}

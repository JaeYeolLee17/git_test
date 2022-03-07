package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private String email;

    private String phone;

    @NotNull
    private AuthorityName authority;
    
}

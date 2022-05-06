package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.annotation.NullOrNotBlank;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.utils.RegExp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NullOrNotBlank
    private String password;

    @NullOrNotBlank
    private String username;

    @Email
    private String email;

    @Pattern(regexp = RegExp.emptyOrPhone)
    private String phone;

    private AuthorityName authority;
    
}

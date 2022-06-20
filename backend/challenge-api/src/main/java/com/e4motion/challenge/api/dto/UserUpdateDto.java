package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.annotation.NullOrNotBlank;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.utils.RegExp;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NullOrNotBlank
    private String username;

    @NullOrNotBlank
    private String oldPassword;

    @Pattern(regexp = RegExp.strongPassword)
    private String newPassword;

    private String nickname;

    @Email
    private String email;

    @Pattern(regexp = RegExp.emptyOrPhone)
    private String phone;

    private Boolean enabled;

    private AuthorityName authority;
    
}

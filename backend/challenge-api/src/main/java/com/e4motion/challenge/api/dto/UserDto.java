package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.utils.RegExpressions;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String username;

    @NotNull
    @Pattern(regexp = RegExpressions.strongPassword)
    private String password;

    private String nickname;

    @Email
    private String email;

    @Pattern(regexp = RegExpressions.emptyOrPhone)
    private String phone;

    private Boolean enabled;

    @NotNull
    private AuthorityName authority;

}

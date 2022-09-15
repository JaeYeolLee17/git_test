package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private Boolean disabled;

    private AuthorityName authority;

}

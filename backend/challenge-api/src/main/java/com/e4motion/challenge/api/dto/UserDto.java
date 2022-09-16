package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.constant.AuthorityName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "사용자 응답 DTO")
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

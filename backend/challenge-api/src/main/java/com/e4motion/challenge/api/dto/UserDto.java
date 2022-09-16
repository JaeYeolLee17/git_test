package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.constant.Lengths;
import com.e4motion.challenge.common.domain.AuthorityName;
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

    @Schema(maxLength = Lengths.USERNAME)
    private String username;

    @Schema(maxLength = Lengths.PASSWORD)
    private String password;

    @Schema(maxLength = Lengths.NICKNAME)
    private String nickname;

    @Schema(maxLength = Lengths.EMAIL)
    private String email;

    private String phone;

    private Boolean disabled;

    private AuthorityName authority;

}

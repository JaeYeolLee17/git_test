package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.constant.Lengths;
import com.e4motion.challenge.common.annotation.NullOrNotBlank;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.utils.RegExpressions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Schema(description = "사용자 수정 요청 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @Schema(maxLength = Lengths.USERNAME)
    @NullOrNotBlank
    @Length(max = Lengths.USERNAME)
    private String username;

    @NullOrNotBlank
    private String oldPassword;

    @Schema(maxLength = Lengths.PASSWORD)
    @Length(max = Lengths.PASSWORD)
    @Pattern(regexp = RegExpressions.strongPassword)
    private String newPassword;

    @Schema(maxLength = Lengths.NICKNAME)
    @Length(max = Lengths.NICKNAME)
    private String nickname;

    @Schema(maxLength = Lengths.EMAIL)
    @Length(max = Lengths.EMAIL)
    @Email
    private String email;

    @Pattern(regexp = RegExpressions.emptyOrPhone)
    private String phone;

    private AuthorityName authority;

}

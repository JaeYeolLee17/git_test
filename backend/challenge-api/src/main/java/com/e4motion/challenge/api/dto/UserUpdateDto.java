package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.constant.FieldLengths;
import com.e4motion.challenge.common.annotation.NullOrNotBlank;
import com.e4motion.challenge.common.constant.AuthorityName;
import com.e4motion.challenge.common.utils.RegExpressions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Schema(description = "사용자 수정 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @Schema(maxLength = FieldLengths.USERNAME)
    @NullOrNotBlank
    @Length(max = FieldLengths.USERNAME)
    private String username;

    @NullOrNotBlank
    private String oldPassword;

    @Schema(maxLength = FieldLengths.PASSWORD)
    @Length(max = FieldLengths.PASSWORD)
    @Pattern(regexp = RegExpressions.strongPassword)
    private String newPassword;

    @Schema(maxLength = FieldLengths.NICKNAME)
    @Length(max = FieldLengths.NICKNAME)
    private String nickname;

    @Schema(maxLength = FieldLengths.EMAIL)
    @Length(max = FieldLengths.EMAIL)
    @Email
    private String email;

    @Pattern(regexp = RegExpressions.emptyOrPhone)
    private String phone;

    @Schema(description = "최고관리자, 운영자만 수정 허용")
    private Boolean disabled;

    @Schema(description = "최고관리자, 운영자만 수정 허용")
    private AuthorityName authority;

}

package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.constant.FieldLengths;
import com.e4motion.challenge.common.constant.AuthorityName;
import com.e4motion.challenge.common.utils.RegExpressions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Schema(description = "사용자 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Schema(maxLength = FieldLengths.USERNAME)
    @NotBlank
    @Length(max = FieldLengths.USERNAME)
    private String username;

    @Schema(maxLength = FieldLengths.PASSWORD)
    @NotNull
    @Length(max = FieldLengths.PASSWORD)
    @Pattern(regexp = RegExpressions.strongPassword)
    private String password;

    @Schema(maxLength = FieldLengths.NICKNAME)
    @Length(max = FieldLengths.NICKNAME)
    private String nickname;

    @Schema(maxLength = FieldLengths.EMAIL)
    @Length(max = FieldLengths.EMAIL)
    @Email
    private String email;

    @Pattern(regexp = RegExpressions.emptyOrPhone)
    private String phone;

    private Boolean disabled;

    @NotNull
    private AuthorityName authority;

}

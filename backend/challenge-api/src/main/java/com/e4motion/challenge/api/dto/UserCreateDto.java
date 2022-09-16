package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.api.constant.Lengths;
import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.utils.RegExpressions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Schema(description = "사용자 등록 요청 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @Schema(maxLength = Lengths.USERNAME)
    @NotBlank
    @Length(max = Lengths.USERNAME)
    private String username;

    @Schema(maxLength = Lengths.PASSWORD)
    @NotNull
    @Length(max = Lengths.PASSWORD)
    @Pattern(regexp = RegExpressions.strongPassword)
    private String password;

    @Schema(maxLength = Lengths.NICKNAME)
    @Length(max = Lengths.NICKNAME)
    private String nickname;

    @Schema(maxLength = Lengths.EMAIL)
    @Length(max = Lengths.EMAIL)
    @Email
    private String email;

    @Pattern(regexp = RegExpressions.emptyOrPhone)
    private String phone;

    @NotNull
    private AuthorityName authority;

}

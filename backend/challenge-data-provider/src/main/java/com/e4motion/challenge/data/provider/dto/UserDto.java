package com.e4motion.challenge.data.provider.dto;

import com.e4motion.challenge.common.domain.AuthorityName;
import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.e4motion.challenge.common.utils.RegExp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;

    @NotBlank
    private String username;

    @NotNull
    @Pattern(regexp = RegExp.strongPassword)
    private String password;

    private String nickname;

    @Email
    private String email;

    @Pattern(regexp = RegExp.emptyOrPhone)
    private String phone;

    private Boolean enabled;

    @NotNull
    private AuthorityName authority;

    @JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
    private LocalDateTime modifiedDate;
}

package com.e4motion.challenge.data.collector.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Schema(description = "카메라 로그인 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraLoginDto {

    @NotBlank
    private String cameraNo;

    @NotBlank
    private String password;
    
}

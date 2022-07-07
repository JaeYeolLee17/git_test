package com.e4motion.challenge.data.collector.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraLoginDto {

    @NotBlank
    private String cameraId;    // TODO: cameraNo 로 변경

    @NotBlank
    private String password;
    
}

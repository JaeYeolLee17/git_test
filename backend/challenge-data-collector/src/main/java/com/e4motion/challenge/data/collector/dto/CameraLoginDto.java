package com.e4motion.challenge.data.collector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraLoginDto {

    @NotEmpty
    private String cameraId;

    @NotEmpty
    private String password;
    
}

package com.e4motion.challenge.data.collector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraLoginDto {

    private String cameraId;
    private String password;
    
}

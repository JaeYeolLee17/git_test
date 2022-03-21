package com.e4motion.challenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntersectionDto {

    @NotBlank
    private String intersectionId;

    @NotBlank
    private String intersectionName;

    private GpsDto gps;

    private Integer nationalId;
}

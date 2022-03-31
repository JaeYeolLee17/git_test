package com.e4motion.challenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

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

    private Point gps;

    private RegionDto regionDto;

    private Integer nationalId;
}

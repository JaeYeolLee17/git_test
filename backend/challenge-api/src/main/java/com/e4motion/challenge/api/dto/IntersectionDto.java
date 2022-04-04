package com.e4motion.challenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Id;
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

    private Double latitude;

    private Double longitude;

    private RegionDto regionDto;

    private Integer nationalId;
}

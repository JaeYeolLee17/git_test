package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
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

package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @NotBlank
    private String regionNo;

    private String regionName;

    //private List<Intersection> intersections;

    private List<GpsDto> gps;
}

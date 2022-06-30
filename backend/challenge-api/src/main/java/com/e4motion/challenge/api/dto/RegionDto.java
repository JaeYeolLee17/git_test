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
    private String regionId;

    @NotBlank
    private String regionName;

    private List<GpsDto> gps;
}

package com.e4motion.challenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
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

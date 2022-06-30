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
public class LinkDto {

    @NotBlank
    private String linkId;

    @NotBlank
    private String startId;

    @NotBlank
    private String endId;

    private String startName;

    private String endName;

    private List<GpsDto> gps;
}

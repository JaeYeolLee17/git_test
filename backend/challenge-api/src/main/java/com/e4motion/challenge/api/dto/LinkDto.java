package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {

    private Long linkId;

    @NotNull
    private IntersectionDto start;

    @NotNull
    private IntersectionDto end;

    private List<GpsDto> gps;

}
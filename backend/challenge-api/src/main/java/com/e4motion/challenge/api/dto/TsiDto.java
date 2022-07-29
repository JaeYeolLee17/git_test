package com.e4motion.challenge.api.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiDto {

    private Long nodeId;

    private GpsDto gps;

    private Boolean error;

    private LocalDateTime time;

    private IntersectionDto intersection;

    private List<TsiSignalDto> tsiSignals;

}

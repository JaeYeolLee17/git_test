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
public class TsiHubDto {

    private Long nodeId;

    private Boolean transition;

    private Boolean response;

    private Boolean lightsOut;

    private Boolean flashing;

    private Boolean manual;

    private Boolean errorScu;

    private Boolean errorCenter;

    private Boolean errorContradiction;

    private Integer cycleCounter;

    private Integer signalCount;

    private LocalDateTime time;

    private List<TsiHubSignalDto> tsiSignals;

}

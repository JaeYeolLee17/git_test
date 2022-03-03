package com.e4motion.challenge.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaneDataDto {

    @Min(TrafficDataDto.MIN_LANE)
    @Max(TrafficDataDto.MAX_LANE)
    private Integer ln;

    private Integer qml;

    @NotNull
    @Size(min = TrafficDataDto.CAR_TYPE, max = TrafficDataDto.CAR_TYPE)
    private Integer[] qm;

    private Float qal;

    @NotNull
    @Size(min = TrafficDataDto.CAR_TYPE, max = TrafficDataDto.CAR_TYPE)
    private Float[] qa;

    @NotNull
    @Size(min = TrafficDataDto.CAR_TYPE, max = TrafficDataDto.CAR_TYPE)
    private Integer[] s;

    @NotNull
    @Size(min = TrafficDataDto.CAR_TYPE, max = TrafficDataDto.CAR_TYPE)
    private Integer[] l;

    @NotNull
    @Size(min = TrafficDataDto.CAR_TYPE, max = TrafficDataDto.CAR_TYPE)
    private Integer[] r;

}

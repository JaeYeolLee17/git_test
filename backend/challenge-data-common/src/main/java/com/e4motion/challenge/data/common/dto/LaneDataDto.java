package com.e4motion.challenge.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaneDataDto {

    private Integer ln;
    private Integer qml;
    private Integer[] qm = new Integer[TrafficDataDto.CAR_TYPE];
    private Float qal;
    private Float[] qa = new Float[TrafficDataDto.CAR_TYPE];
    private Integer[] s = new Integer[TrafficDataDto.CAR_TYPE];
    private Integer[] l = new Integer[TrafficDataDto.CAR_TYPE];
    private Integer[] r = new Integer[TrafficDataDto.CAR_TYPE];

}

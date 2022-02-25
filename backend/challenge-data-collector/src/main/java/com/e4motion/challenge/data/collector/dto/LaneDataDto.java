package com.e4motion.challenge.data.collector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaneDataDto {

    private Integer ln;
    private Integer qml;
    private Integer[] qm = new Integer[DataDto.CAR_TYPE];
    private Float qal;
    private Float[] qa = new Float[DataDto.CAR_TYPE];
    private Integer[] s = new Integer[DataDto.CAR_TYPE];
    private Integer[] l = new Integer[DataDto.CAR_TYPE];
    private Integer[] r = new Integer[DataDto.CAR_TYPE];

}

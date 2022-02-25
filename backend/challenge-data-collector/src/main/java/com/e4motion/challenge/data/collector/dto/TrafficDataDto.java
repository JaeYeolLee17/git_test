package com.e4motion.challenge.data.collector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDataDto {

    private String st;
    private String et;
    private Integer p;
    private Integer[] u = new Integer[DataDto.CAR_TYPE];
    private List<LaneDataDto> ld;

}

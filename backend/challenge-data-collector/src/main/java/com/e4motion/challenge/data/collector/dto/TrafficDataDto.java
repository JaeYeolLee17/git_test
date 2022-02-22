package com.e4motion.challenge.data.collector.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrafficDataDto {

    private String st;
    private String et;
    private Integer p;
    private Integer[] u = new Integer[DataDto.CAR_TYPE];
    private List<LaneDataDto> ld;

}

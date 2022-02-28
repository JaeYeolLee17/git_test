package com.e4motion.challenge.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDataDto {

    public final static int CAR_TYPE = 5;

    private String st;
    private String et;
    private Integer p;
    private Integer[] u = new Integer[CAR_TYPE];
    private List<LaneDataDto> ld;

}

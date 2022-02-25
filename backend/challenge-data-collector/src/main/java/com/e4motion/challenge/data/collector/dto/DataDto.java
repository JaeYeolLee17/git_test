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
public class DataDto {

    public final static int CAR_TYPE = 5;

    private String v;
    private String c;
    private String i;
    private String r;
    private String t;
    private List<TrafficDataDto> td;

}

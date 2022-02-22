package com.e4motion.challenge.data.collector.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataDto {

    public final static int CAR_TYPE = 5;

    private String v;
    private String c;
    private String i;
    private String r;
    private String t;
    private List<TrafficDataDto> td;

}

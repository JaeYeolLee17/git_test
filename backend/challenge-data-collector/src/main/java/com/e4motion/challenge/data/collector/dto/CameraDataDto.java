package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDataDto {

    private String v;
    private String c;
    private String i;
    private String r;
    private String t;
    private List<TrafficDataDto> td;

}

package com.e4motion.challenge.data.provider.dto;

import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {

    private String v;
    private String c;
    private String i;
    private String r;
    private String t;
    private TrafficDataDto td;

}

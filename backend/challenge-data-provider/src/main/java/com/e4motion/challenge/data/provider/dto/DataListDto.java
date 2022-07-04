package com.e4motion.challenge.data.provider.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataListDto {

    private String nextTime;
    private List<DataDto> data;
}

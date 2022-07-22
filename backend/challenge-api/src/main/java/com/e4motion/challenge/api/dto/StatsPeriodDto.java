package com.e4motion.challenge.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsPeriodDto {

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer dayOfWeek;

    private Integer weekOfYear;

    private String cameraNo;

    private String intersectionNo;

    private String regionNo;

    private List<StatsPeriodDataDto> data;

}

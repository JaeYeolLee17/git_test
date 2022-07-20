package com.e4motion.challenge.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsMfdDto {

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer dayOfWeek;

    private String cameraNo;

    private String intersectionNo;

    private String regionNo;

    private List<StatsMfdDataDto> data;

}

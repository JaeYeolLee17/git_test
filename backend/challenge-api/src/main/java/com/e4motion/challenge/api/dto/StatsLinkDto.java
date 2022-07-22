package com.e4motion.challenge.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsLinkDto {

    private Integer year;

    private Integer month;

    private Integer day;

    private String cameraNo;

    private LinkDto link;

    private List<StatsLinkDataDto> data;

}

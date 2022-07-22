package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsLinkDataDto {

    private Integer hour;

    private Integer min;

    private Integer srlu;

    private Integer qtsrlu;

    private Integer sr;

    private Integer qtsr;

    private Integer lu;

    private Integer qtlu;

}

package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsPeriodDataDto {

    private Integer hour;

    private Integer p;

    private Integer srlu;

    private Integer qtsrlu;

}

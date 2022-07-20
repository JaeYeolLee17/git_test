package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsMfdDataDto {

    private Integer hour;

    private Integer min;

    private Integer srlu;

    private Integer qtsrlu;

}

package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiHubSignalDto {

    private TsiSignalInfo info;

    private TsiTimeReliability timeReliability;

    private Boolean person;

    private TsiSignalStatus status;

    private Integer displayTime;

    private Integer remainTime;

    private Integer direction;

}

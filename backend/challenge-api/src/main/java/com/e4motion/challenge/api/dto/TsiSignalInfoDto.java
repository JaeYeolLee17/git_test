package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiSignalInfoDto {

    private TsiSignalInfo info;

    private TsiSignalStatus status;

}

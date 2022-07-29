package com.e4motion.challenge.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiSignalDto {

    private Integer direction;

    private List<TsiSignalInfoDto> tsiSignalInfos;

}

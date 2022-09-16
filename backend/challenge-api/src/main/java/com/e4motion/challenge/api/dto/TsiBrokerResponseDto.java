package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiBrokerResponseDto {

    private String resultCode;

    private String resultDesc;

    private TsiBrokerDto resultData;

}

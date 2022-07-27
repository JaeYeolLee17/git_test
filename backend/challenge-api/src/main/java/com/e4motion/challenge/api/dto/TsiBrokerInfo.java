package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiBrokerInfo {

    private String host;

    private Integer port;

    private String id;

    private String pwd;

}

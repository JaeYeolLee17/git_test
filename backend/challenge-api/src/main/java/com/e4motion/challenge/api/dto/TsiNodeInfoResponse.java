package com.e4motion.challenge.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiNodeInfoResponse {

    private String resultCode;

    private String resultDesc;

    private List<TsiNodeInfo> resultData;

}

package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TsiNodeDto {

    private Long node_id;

    private String node_name;

    private Double latitude;

    private Double longitude;
}

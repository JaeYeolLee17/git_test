package com.e4motion.challenge.api.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraRoadDto {

    private String startLine;

    private String[] lane;

    private String uturn;

    private String crosswalk;

    private Boolean[][] direction;

}
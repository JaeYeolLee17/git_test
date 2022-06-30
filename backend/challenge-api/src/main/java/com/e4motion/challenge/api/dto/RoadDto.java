package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadDto {

    @NotBlank
    private String roadId;

    private String cameraId;

    private String startLine;

    private String uturn;

    private String crosswalk;

    private List<RoadLaneDto> lanes;

    private List<RoadDirectionDto> directions;
}

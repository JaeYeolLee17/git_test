package com.e4motion.challenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
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

package com.e4motion.challenge.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDto {

    @NotBlank
    private String cameraId;

    @NotBlank
    private String password;

    private IntersectionDto intersection;

    private IntersectionDto direction;

    private Point gps;

    private RoadDto road;

    private String rtspUrl;

    private String rtspId;

    private String rtspPassword;

    private String serverUrl;

    private Integer distance;

    private Integer sendCycle;

    private Integer collectCycle;

    private Integer smallWidth;

    private Integer smallHeight;

    private Integer largeWidth;

    private Integer largeHeight;

    private Integer degree;

    private Timestamp lastDataTime;

    private boolean settingsUpdated;
}

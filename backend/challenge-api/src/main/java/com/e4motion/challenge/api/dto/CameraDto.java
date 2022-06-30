package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
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

    private Double latitude;

    private Double longitude;

    private String rtspUrl;

    private String serverUrl;

    private Integer collectCycle;

    private String rtspId;

    private String rtspPassword;

    private Integer sendCycle;

    private Integer distance;

    private Timestamp lastDataTime;

    private Integer smallWidth;

    private Integer smallHeight;

    private Integer largeWidth;

    private Integer largeHeight;

    private Integer degree;

    private boolean settingsUpdated;

    private RoadDto road;
}

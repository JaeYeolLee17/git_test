package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.utils.RegExp;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDto {

    @NotBlank
    private String cameraNo;

    @NotNull
    @Pattern(regexp = RegExp.strongPassword)
    private String password;

    private IntersectionDto intersection;

    private IntersectionDto direction;

    private GpsDto gps;

    private Integer distance;

    private String rtspUrl;

    private String rtspId;

    private String rtspPassword;

    private String serverUrl;

    private Integer sendCycle;

    private Integer collectCycle;

    private Integer smallWidth;

    private Integer smallHeight;

    private Integer largeWidth;

    private Integer largeHeight;

    private Integer degree;

    private Boolean settingsUpdated;

    private LocalDateTime lastDataTime;

    private CameraRoadDto road;

    @Override
    public String toString() {

        String intersectionNo = (intersection == null) ? null : intersection.getIntersectionNo();
        String directionNo = (direction == null) ? null : direction.getIntersectionNo();

        return "CameraDto{" +
                "cameraNo='" + cameraNo + '\'' +
                ", password='" + password + '\'' +
                ", intersection=" + intersectionNo +
                ", direction=" + directionNo +
                ", gps=" + gps +
                ", distance=" + distance +
                ", rtspUrl='" + rtspUrl + '\'' +
                ", rtspId='" + rtspId + '\'' +
                ", rtspPassword='" + rtspPassword + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", sendCycle=" + sendCycle +
                ", collectCycle=" + collectCycle +
                ", smallWidth=" + smallWidth +
                ", smallHeight=" + smallHeight +
                ", largeWidth=" + largeWidth +
                ", largeHeight=" + largeHeight +
                ", degree=" + degree +
                ", settingsUpdated=" + settingsUpdated +
                ", lastDataTime=" + lastDataTime +
                ", road=" + road +
                '}';
    }
}
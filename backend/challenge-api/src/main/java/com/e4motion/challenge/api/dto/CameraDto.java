package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.constant.FieldLengths;
import com.e4motion.challenge.common.utils.RegExpressions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Schema(description = "카메라 DTO")
@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDto {

    @Schema(maxLength = FieldLengths.CAMERA_NO)
    @NotBlank
    @Length(max = FieldLengths.CAMERA_NO)
    private String cameraNo;

    @Schema(maxLength = FieldLengths.PASSWORD)
    @NotNull
    @Length(max = FieldLengths.PASSWORD)
    @Pattern(regexp = RegExpressions.strongPassword)
    private String password;

    @Valid
    private IntersectionDto intersection;

    @Valid
    private IntersectionDto direction;

    @Valid
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
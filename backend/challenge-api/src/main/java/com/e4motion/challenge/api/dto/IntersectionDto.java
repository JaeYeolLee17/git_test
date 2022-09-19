package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.constant.FieldLengths;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Schema(description = "교차로 DTO")
@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntersectionDto {

    @Schema(maxLength = FieldLengths.INTERSECTION_NO)
    @NotBlank
    @Length(max = FieldLengths.INTERSECTION_NO)
    private String intersectionNo;

    @Schema(maxLength = FieldLengths.INTERSECTION_NAME)
    @Length(max = FieldLengths.INTERSECTION_NAME)
    private String intersectionName;

    @Valid
    private GpsDto gps;

    @Valid
    private RegionDto region;

    private Long nationalId;

    @Schema(description = "교차로 정보 응답 시에만 카메라 목록 포함함. 교차로 정보 생성 및 수정 시 사용 안함.")
    private List<CameraDto> cameras;

    @Override
    public String toString() {

        String regionNo = (region == null) ? null : region.getRegionNo();

        return "IntersectionDto{" +
                "intersectionNo='" + intersectionNo + '\'' +
                ", intersectionName='" + intersectionName + '\'' +
                ", gps=" + gps +
                ", region=" + regionNo +
                ", nationalId=" + nationalId +
                ", cameras=" + cameras +
                '}';
    }
}

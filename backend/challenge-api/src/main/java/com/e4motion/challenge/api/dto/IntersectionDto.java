package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntersectionDto {

    @NotBlank
    private String intersectionNo;

    @NotBlank
    private String intersectionName;

    private GpsDto gps;

    private RegionDto region;

    private Long nationalId;

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

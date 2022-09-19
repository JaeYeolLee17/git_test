package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.constant.FieldLengths;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Schema(description = "구역 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @Schema(maxLength = FieldLengths.REGION_NO)
    @NotBlank
    @Length(max = FieldLengths.REGION_NO)
    private String regionNo;

    @Schema(maxLength = FieldLengths.REGION_NAME)
    @Length(max = FieldLengths.REGION_NAME)
    private String regionName;

    @Valid
    private List<GpsDto> gps;

    @Schema(description = "구역 정보 응답 시에만 포함")
    private List<IntersectionDto> intersections;

}

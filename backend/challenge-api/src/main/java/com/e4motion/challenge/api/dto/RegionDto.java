package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto {

    @NotBlank
    private String regionNo;

    @NotBlank
    private String regionName;

    private List<GpsDto> gps;

    private List<IntersectionDto> intersections;

    @JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
    private LocalDateTime modifiedDate;
}

package com.e4motion.challenge.api.dto;

import com.e4motion.challenge.common.utils.DateTimeHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {

    @Min(1)
    private Long linkId;

    @NotNull
    private IntersectionDto start;

    @NotNull
    private IntersectionDto end;

    private List<GpsDto> gps;

    @JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = DateTimeHelper.dateTimeFormat, shape = JsonFormat.Shape.STRING)
    private LocalDateTime modifiedDate;
}
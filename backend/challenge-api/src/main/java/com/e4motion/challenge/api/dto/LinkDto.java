package com.e4motion.challenge.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "링크 DTO")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {

    private Long linkId;

    @NotNull
    @Valid
    private IntersectionDto start;

    @NotNull
    @Valid
    private IntersectionDto end;

    @Valid
    private List<GpsDto> gps;

}
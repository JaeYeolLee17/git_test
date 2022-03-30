package com.e4motion.challenge.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {

    @NotBlank
    private String linkId;

    @NotBlank
    private String startId;

    @NotBlank
    private String endId;

    private String startName;

    private String endName;

    private List<Point> gps;
}

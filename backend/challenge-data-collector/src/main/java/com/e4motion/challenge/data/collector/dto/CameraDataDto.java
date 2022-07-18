package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.common.utils.RegExpressions;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDataDto {

    @NotBlank
    private String v;

    @NotBlank
    private String c;

    private String i;

    private String r;

    @NotNull
    @Pattern(regexp = RegExpressions.dateTime)
    private String t;

    @NotNull
    @Size(min = 1)
    private @Valid List<TrafficDataDto> td;

}

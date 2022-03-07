package com.e4motion.challenge.data.common.dto;

import com.e4motion.challenge.common.utils.RegExp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDataDto {

    public final static int CAR_TYPE = 5;

    public final static int MIN_LANE = 1;
    public final static int MAX_LANE = 10;

    @NotNull
    @Pattern(regexp = RegExp.dateTime)
    private String st;

    @NotNull
    @Pattern(regexp = RegExp.dateTime)
    private String et;

    private Integer p;

    @NotNull
    @Size(min = TrafficDataDto.CAR_TYPE, max = TrafficDataDto.CAR_TYPE)
    private Integer[] u;

    @NotNull
    @Size(min = 1)
    private @Valid List<LaneDataDto> ld;

}

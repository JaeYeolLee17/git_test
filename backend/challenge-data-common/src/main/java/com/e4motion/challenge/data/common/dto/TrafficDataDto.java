package com.e4motion.challenge.data.common.dto;

import com.e4motion.challenge.common.utils.RegExpressions;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDataDto {

    public final static int CAR_TYPE = 5;

    public final static int MIN_LANE = 1;
    public final static int MAX_LANE = 12;

    @NotNull
    @Pattern(regexp = RegExpressions.dateTime)
    private String st;

    @NotNull
    @Pattern(regexp = RegExpressions.dateTime)
    private String et;

    private Integer p;

    @NotNull
    @Size(min = CAR_TYPE, max = CAR_TYPE)
    private Integer[] u;

    @NotNull
    @Size(min = MIN_LANE, max = MAX_LANE)
    @Valid
    private List<LaneDataDto> ld;

}

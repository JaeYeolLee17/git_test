package com.e4motion.challenge.data.collector.dto;

import com.e4motion.challenge.common.utils.RegExp;
import com.e4motion.challenge.data.common.dto.TrafficDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CameraDataDto {

    @NotEmpty
    private String v;

    @NotEmpty
    private String c;

    private String i;

    private String r;

    @NotNull
    @Pattern(regexp = RegExp.dateTime)
    private String t;

    @NotNull
    @Size(min = 1)
    private @Valid List<TrafficDataDto> td;

}

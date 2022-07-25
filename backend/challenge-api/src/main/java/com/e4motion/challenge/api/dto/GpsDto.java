package com.e4motion.challenge.api.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GpsDto {

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

}

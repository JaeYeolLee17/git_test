package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_region_gps")
public class Gps {

    @Id
    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;
}

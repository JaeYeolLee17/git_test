package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_region_gps")
public class RegionGps {

    @Id
    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private Region region;
}

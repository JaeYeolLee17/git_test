package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_intersection")
public class Intersection {

    @Id
    @Column(name = "intersection_id", length = 10)
    private String intersectionId;

    @Column(name = "intersection_name", length = 20)
    private String intersectionName;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "national_id")
    private Integer nationalId;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
}

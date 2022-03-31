package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "gps")
    private Point gps;

    @Column(name = "region_id", length = 10)
    private String regionId;

    @Column(name = "national_id")
    private Integer nationalId;
}

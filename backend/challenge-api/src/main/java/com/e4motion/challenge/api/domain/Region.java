package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_region")
public class Region extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "region_no", length = 10, unique = true, nullable = false)
    private String regionNo;

    @Column(name = "region_name", length = 32)
    private String regionName;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("gps_order ASC")
    private List<RegionGps> gps;

    @OneToMany(mappedBy = "region")
    private List<Intersection> intersections;

}
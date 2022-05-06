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
public class Region {

    @Id
    @Column(name = "region_id", length = 10)
    private String regionId;

    @Column(name = "region_name", length = 20)
    private String regionName;

    @OneToMany(mappedBy = "region")
    private List<Intersection> intersections;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id")
    private List<RegionGps> gps;
}

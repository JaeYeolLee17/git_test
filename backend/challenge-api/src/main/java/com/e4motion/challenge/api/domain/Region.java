package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
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

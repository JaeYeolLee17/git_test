package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

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

    @Column(name = "gps")
    @ElementCollection(targetClass=Point.class)
    private List<Point> gps;

}

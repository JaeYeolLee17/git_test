package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_intersection")
public class Intersection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intersection_id")
    private Long intersectionId;

    @Column(name = "intersection_no", length = 10, unique = true, nullable = false)
    private String intersectionNo;

    @Column(name = "intersection_name", length = 32)
    private String intersectionName;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "national_id")
    private Long nationalId;

    @OneToMany(mappedBy = "intersection")
    private List<Camera> cameras;

    @Override
    public String toString() {
        return "Intersection{" +
                "intersectionId=" + intersectionId +
                ", intersectionNo='" + intersectionNo + '\'' +
                ", intersectionName='" + intersectionName + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", region=" + region.getRegionNo() +
                ", nationalId=" + nationalId +
                ", cameras=" + cameras +
                '}';
    }
}
package com.e4motion.challenge.api.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_region_gps", uniqueConstraints = {@UniqueConstraint(columnNames = {"region_id", "latitude", "longitude"})})
public class RegionGps extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_gps_id")
    private Long regionGpsId;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "gps_order")
    private Integer gpsOrder;

    @Override
    public String toString() {
        return "RegionGps{" +
                "regionGpsId=" + regionGpsId +
                ", region=" + region.getRegionNo() +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", gpsOrder=" + gpsOrder +
                '}';
    }
}

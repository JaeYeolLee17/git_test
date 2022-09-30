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
@Table(name = "nt_link_gps", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"link_id", "lat", "lng"}),
        @UniqueConstraint(columnNames = {"link_id", "gps_order"})
})
public class LinkGps extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "link_gps_id")
    private Long linkGpsId;

    @ManyToOne
    @JoinColumn(name = "link_id")
    private Link link;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "gps_order")
    private Integer gpsOrder;

    @Override
    public String toString() {
        return "LinkGps{" +
                "linkGpsId=" + linkGpsId +
                ", link=" + link.getStart().getIntersectionNo() + "->" + link.getEnd().getIntersectionNo() +
                ", lat=" + lat +
                ", lng=" + lng +
                ", gpsOrder=" + gpsOrder +
                '}';
    }
}

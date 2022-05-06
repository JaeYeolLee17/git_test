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
@Table(name = "nt_camera_road_lane")
public class RoadLane {

    @Id
    @Column(name = "road_id")
    private String road_id;

    @Column(name = "lane")
    private String lane;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "road_id", insertable = false, updatable = false)
    private Road road;
}

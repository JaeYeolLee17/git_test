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
@Table(name = "nt_camera_road")
public class Road {

    @Id
    @Column(name = "road_id", length = 10)
    private String roadId;

    @Column(name = "camera_id", length = 10)
    private String cameraId;

    @Column(name = "start_line", length = 128)
    private String startLine;

    @Column(name = "uturn", length = 128)
    private String uturn;

    @Column(name = "crosswalk", length = 128)
    private String crosswalk;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "road_id")
    private List<RoadLane> lanes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "road_id")
    private List<RoadDirection> directions;

}

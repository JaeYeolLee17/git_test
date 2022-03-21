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

    @Column(name = "lane")
    @ElementCollection(targetClass= String.class)
    private List<String> lane;

    @Column(name = "direction", length = 128)
    @ElementCollection(targetClass= String.class)
    private List<String> direction;
}

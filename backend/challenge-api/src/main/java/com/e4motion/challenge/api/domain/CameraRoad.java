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
@Table(name = "nt_camera_road")
public class CameraRoad extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camera_road_id")
    private Long cameraRoadId;

    @OneToOne
    @JoinColumn(name = "camera_id")
    private Camera camera;

    @Column(name = "start_line", length = 128)
    private String startLine;

    @Column(name = "lane", length = 256)
    private String lane;

    @Column(name = "uturn", length = 128)
    private String uturn;

    @Column(name = "crosswalk", length = 128)
    private String crosswalk;

    @Column(name = "direction", length = 256)
    private String direction;

    @Override
    public String toString() {
        return "CameraRoad{" +
                "cameraRoadId=" + cameraRoadId +
                ", camera=" + camera.getCameraNo() +
                ", startLine='" + startLine + '\'' +
                ", lane='" + lane + '\'' +
                ", uturn='" + uturn + '\'' +
                ", crosswalk='" + crosswalk + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
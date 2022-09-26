package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.common.constant.FieldLengths;
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

    @Column(name = "start_line", length = FieldLengths.CAMERA_START_LINE)
    private String startLine;

    @Column(name = "lane", length = FieldLengths.CAMERA_LANE)
    private String lane;

    @Column(name = "uturn", length = FieldLengths.CAMERA_UTURN)
    private String uturn;

    @Column(name = "crosswalk", length = FieldLengths.CAMERA_CROSSWALK)
    private String crosswalk;

    @Column(name = "direction", length = FieldLengths.CAMERA_DIRECTION)
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
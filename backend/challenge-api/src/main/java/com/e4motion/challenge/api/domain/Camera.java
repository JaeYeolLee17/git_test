package com.e4motion.challenge.api.domain;

import com.e4motion.challenge.common.constant.FieldLengths;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
//@ToString                             // remove circular reference.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_camera")
public class Camera extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camera_id")
    private Long cameraId;

    @Column(name = "camera_no", length = FieldLengths.CAMERA_NO, unique = true, nullable = false)
    private String cameraNo;

    @Column(name = "password", length = FieldLengths.PASSWORD, nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "intersection_id")
    private Intersection intersection;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Intersection direction;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "distance")
    private Integer distance;

    @Column(name = "rtsp_url", length = 128)
    private String rtspUrl;

    @Column(name = "rtsp_id", length = 32)
    private String rtspId;

    @Column(name = "rtsp_password", length = 128)
    private String rtspPassword;

    @Column(name = "server_url", length = 128)
    private String serverUrl;

    @Column(name = "send_cycle")
    private Integer sendCycle;

    @Column(name = "collect_cycle")
    private Integer collectCycle;

    @Column(name = "small_width")
    private Integer smallWidth;

    @Column(name = "small_height")
    private Integer smallHeight;

    @Column(name = "large_width")
    private Integer largeWidth;

    @Column(name = "large_height")
    private Integer largeHeight;

    @Column(name = "degree")
    private Integer degree;

    @Column(name = "settings_updated", nullable = false)
    private Boolean settingsUpdated;

    @Column(name = "last_data_time")
    private LocalDateTime lastDataTime;

    @OneToOne(mappedBy = "camera", cascade = CascadeType.ALL, orphanRemoval = true)
    private CameraRoad road;

    @Override
    public String toString() {
        return "Camera{" +
                "cameraId=" + cameraId +
                ", cameraNo='" + cameraNo + '\'' +
                ", password='" + password + '\'' +
                ", intersection=" + intersection.getIntersectionNo() +
                ", direction=" + direction.getIntersectionNo() +
                ", lat=" + lat +
                ", lng=" + lng +
                ", distance=" + distance +
                ", rtspUrl='" + rtspUrl + '\'' +
                ", rtspId='" + rtspId + '\'' +
                ", rtspPassword='" + rtspPassword + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", sendCycle=" + sendCycle +
                ", collectCycle=" + collectCycle +
                ", smallWidth=" + smallWidth +
                ", smallHeight=" + smallHeight +
                ", largeWidth=" + largeWidth +
                ", largeHeight=" + largeHeight +
                ", degree=" + degree +
                ", settingsUpdated=" + settingsUpdated +
                ", lastDataTime=" + lastDataTime +
                ", road=" + road +
                '}';
    }
}
package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_camera")
public class Camera {
	
	@Id
	@Column(name = "camera_id", length = 10)
	private String cameraId;

	@Column(name = "password", length = 256, nullable = false)
	private String password;

	@ManyToOne
	@JoinColumn(name = "intersection_id")
	private Intersection intersection;

	@ManyToOne
	@JoinColumn(name = "direction_id")
	private Intersection direction;

	@Column(name = "latitude")
	private double latitude;

	@Column(name = "longitude")
	private double longitude;

	@Column(name = "rtsp_url", length = 128)
	private String rtspUrl;

	@Column(name = "server_url", length = 128)
	private String serverUrl;

	@Column(name = "collect_cycle")
	private Integer collectCycle;

	@Column(name = "rtsp_id", length = 10)
	private String rtspId;

	@Column(name = "rtsp_password", length = 256, nullable = false)
	private String rtspPassword;

	@Column(name = "send_cycle")
	private Integer sendCycle;

	@Column(name = "distance")
	private Integer distance;

	@Column(name = "last_data_time")
	private Timestamp lastDataTime;

	@Column(name = "s_width")
	private Integer smallWidth;

	@Column(name = "s_height")
	private Integer smallHeight;

	@Column(name = "l_width")
	private Integer largeWidth;

	@Column(name = "l_height")
	private Integer largeHeight;

	@Column(name = "degree")
	private Integer degree;

   	@Column(name = "settings_updated")
   	private boolean settingsUpdated;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "camera_id")
	private Road road;

}

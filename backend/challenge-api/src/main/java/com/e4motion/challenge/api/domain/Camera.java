package com.e4motion.challenge.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	@Column(name = "intersection_id", length = 10)
	private String intersectionId;

	@Column(name = "lat")
	private double lat;

	@Column(name = "lng")
	private double lng;

	@Column(name = "direction", length = 10)
	private String direction;

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

}

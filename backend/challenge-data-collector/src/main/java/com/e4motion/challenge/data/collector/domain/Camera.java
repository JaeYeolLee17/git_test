package com.e4motion.challenge.data.collector.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_camera")
public class Camera {

	@Id
	@Column(name = "camera_id", length = 10)
	private String cameraId;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "settings_updated")
	private Boolean settingsUpdated;
   
}

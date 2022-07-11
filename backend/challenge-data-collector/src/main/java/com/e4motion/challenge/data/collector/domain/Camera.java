package com.e4motion.challenge.data.collector.domain;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "camera_id")
	private Long cameraId;

	@Column(name = "camera_no", length = 10, unique = true, nullable = false)
	private String cameraNo;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "settings_updated", nullable = false)
	private Boolean settingsUpdated;
   
}

package com.e4motion.challenge.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "nt_camera")
public class Camera {
	
	@Id
	@Column(name = "camera_id")
	private String cameraId;

	@Column(name = "password")
	private String password;
	
	@Column(name = "intersection_id")
	private String intersectionId;
	
	
	// TODO: more columns...
	
   
   	@Column(name = "settings_updated")
   	private boolean settingsUpdated;
   
}

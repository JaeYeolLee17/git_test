package com.e4motion.challenge.data.collector.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nt_authority")
public class Authority {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_MANAGER = "ROLE_MANAGER";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_DATA = "ROLE_DATA";
	public static final String ROLE_CAMERA_DATA = "ROLE_CAMERA_DATA";
	public static final String ROLE_CAMERA = "ROLE_CAMERA";
	public static final String ROLE_AVL_CAR = "ROLE_AVL_CAR";
	public static final String ROLE_ITS = "ROLE_ITS";
	
	@Id
	@Column(name = "authority_name", length = 20)
	private String authorityName;
   
}

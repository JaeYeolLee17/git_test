package com.e4motion.challenge.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	   
	//@Id
	//@Column(name = "authority_name", length = 50)
	private String authorityName;
   
}

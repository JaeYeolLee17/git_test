package com.e4motion.challenge.auth.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
//@Entity
public class User {

	//@Id 
	//@GeneratedValue
	private int id;
	
	//@Version
	private int version;
	
	private String userId;
	
	private String name;

}

package com.e4motion.challenge.auth.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
//@Entity
public class User {

	//@Id 
	//@GeneratedValue
	private int id;
	
	//@Version
	private int version;
	
	private String userId;
	
	private String name;
	
	@Builder
	public User(String userId, String name) {
		this.userId = userId;
		this.name = name;
	}

}

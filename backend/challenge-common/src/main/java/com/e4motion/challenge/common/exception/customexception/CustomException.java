package com.e4motion.challenge.common.exception.customexception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
    
	public CustomException(String code, String message) {
		super(message);
		this.code = code;
	}
	
}

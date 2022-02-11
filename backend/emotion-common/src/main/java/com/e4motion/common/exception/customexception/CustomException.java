package com.e4motion.common.exception.customexception;

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

	public String getCode() {	// TODO: lombok
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

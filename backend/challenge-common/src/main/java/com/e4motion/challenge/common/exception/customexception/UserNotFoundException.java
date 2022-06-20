package com.e4motion.challenge.common.exception.customexception;

public class UserNotFoundException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "NONEXISTENT_USER";

	public static final String INVALID_USERNAME = "Invalid username";

	public UserNotFoundException(String message) {
		super(CODE, message);
	}

}

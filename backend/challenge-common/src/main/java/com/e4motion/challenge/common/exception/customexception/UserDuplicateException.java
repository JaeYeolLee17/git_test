package com.e4motion.challenge.common.exception.customexception;

public class UserDuplicateException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "ALREADY_EXISTENT_USER";

	public static final String USERNAME_ALREADY_EXISTS = "Username already exists";

	public UserDuplicateException(String message) {
		super(CODE, message);
	}

}

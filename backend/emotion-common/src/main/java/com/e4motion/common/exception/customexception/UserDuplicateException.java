package com.e4motion.common.exception.customexception;

public class UserDuplicateException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "ALREADY_EXISTENT_USER";

	public static final String USER_ID_ALREADY_EXISTS = "User id already exists";

	public UserDuplicateException(String message) {
		super(CODE, message);
	}

}

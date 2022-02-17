package com.e4motion.common.exception.customexception;

public class UserDuplicateException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "ALREADY_EXIST_USER";
	
	public UserDuplicateException(String message) {
		super(CODE, message);
	}

}

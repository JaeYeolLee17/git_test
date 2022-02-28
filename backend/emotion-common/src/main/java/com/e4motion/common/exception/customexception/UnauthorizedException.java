package com.e4motion.common.exception.customexception;

public class UnauthorizedException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "UNAUTHORIZED_TOKEN";

	public static final String UNAUTHORIZED_TOKEN = "Unauthorized token";
	public static final String INVALID_PASSWORD = "Invalid password";

	public UnauthorizedException(String message) {
		super(CODE, message);
	}

}

package com.e4motion.challenge.common.exception.customexception;

public class UnauthorizedException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "UNAUTHORIZED";

	public static final String UNAUTHORIZED_TOKEN = "Unauthorized token";
	public static final String INVALID_PASSWORD = "Invalid password";
	public static final String DISABLED_USER = "Disabled user";

	public UnauthorizedException(String message) {
		super(CODE, message);
	}

}

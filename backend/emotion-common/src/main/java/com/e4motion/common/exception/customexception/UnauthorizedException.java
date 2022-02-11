package com.e4motion.common.exception.customexception;

public class UnauthorizedException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "UNAUTHORIZED_TOKEN";
	
	public UnauthorizedException(String message) {
		super(CODE, message);
	}

}

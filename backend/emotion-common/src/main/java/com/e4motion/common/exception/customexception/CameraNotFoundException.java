package com.e4motion.common.exception.customexception;

public class CameraNotFoundException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "NONEXISTENT_CAMERA";
	
	public CameraNotFoundException(String message) {
		super(CODE, message);
	}

}

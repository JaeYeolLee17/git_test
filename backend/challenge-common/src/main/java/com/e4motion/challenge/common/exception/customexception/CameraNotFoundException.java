package com.e4motion.challenge.common.exception.customexception;

public class CameraNotFoundException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "NONEXISTENT_CAMERA";

	public static final String INVALID_CAMERA_NO = "Invalid camera no";

	public CameraNotFoundException(String message) {
		super(CODE, message);
	}

}

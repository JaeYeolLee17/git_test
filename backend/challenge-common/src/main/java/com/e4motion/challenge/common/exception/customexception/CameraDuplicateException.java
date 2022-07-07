package com.e4motion.challenge.common.exception.customexception;

public class CameraDuplicateException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "ALREADY_EXISTENT_CAMERA";

	public static final String CAMERA_NO_ALREADY_EXISTS = "Camera no already exists";

	public CameraDuplicateException(String message) {
		super(CODE, message);
	}

}

package com.e4motion.common.exception.customexception;

public class CameraDuplicateException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "ALREADY_EXIST_CAMERA";
	
	public CameraDuplicateException(String message) {
		super(CODE, message);
	}

}

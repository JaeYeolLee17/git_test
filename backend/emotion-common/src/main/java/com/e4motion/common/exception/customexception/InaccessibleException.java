package com.e4motion.common.exception.customexception;

public class InaccessibleException extends CustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String CODE = "INACCESSIBLE_DATA";
	
	public InaccessibleException(String message) {
		super(CODE, message);
	}

}

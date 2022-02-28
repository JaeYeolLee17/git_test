package com.e4motion.common.exception.customexception;

public class InvalidParamException extends CustomException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String CODE = "INVALID_PARAM";

	public static final String INVALID_DATA = "Invalid data";

	public static final String INVALID_LIMIT = "Invalid limit";

	public InvalidParamException(String message) {
		super(CODE, message);
	}

}

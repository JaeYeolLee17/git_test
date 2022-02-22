package com.e4motion.common.exception.customexception;

public class InvalidParamException extends CustomException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String CODE = "INVALID_PARAM";

	public InvalidParamException(String message) {
		super(CODE, message);
	}

}

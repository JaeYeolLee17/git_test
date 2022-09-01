package com.e4motion.challenge.common.exception.customexception;

public class OpenWeatherException extends CustomException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String CODE = "NONEXISTENT_URL";

	public static final String NOT_FOUND = "Invalid api url";

	public static final String UNAUTHORIZED = "Invalid api key";

	public OpenWeatherException(String message) {
		super(CODE, message);
	}

}

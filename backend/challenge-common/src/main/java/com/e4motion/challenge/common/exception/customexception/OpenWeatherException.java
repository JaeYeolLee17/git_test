package com.e4motion.challenge.common.exception.customexception;

public class OpenWeatherException extends CustomException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String CODE = "OPEN_WEATHER_ERROR";

	public static final String INVALID_API_URL = "Invalid api url";

	public static final String INVALID_API_KEY = "Invalid api key";

	public OpenWeatherException(String message) {
		super(CODE, message);
	}

}

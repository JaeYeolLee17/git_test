package com.e4motion.challenge.common.exception.customexception;

public class OpenWeatherException extends CustomException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// TODO: by sjkim
	// TODO: CODE 내용이 이상한걸요?
	public static final String CODE = "NONEXISTENT_URL";

	// TODO: 아래 두 메시지 사용하는 곳이 없네요.... 확인 후 삭제!!
	public static final String NOT_FOUND = "Invalid api url";

	public static final String UNAUTHORIZED = "Invalid api key";

	public OpenWeatherException(String message) {
		super(CODE, message);
	}

}

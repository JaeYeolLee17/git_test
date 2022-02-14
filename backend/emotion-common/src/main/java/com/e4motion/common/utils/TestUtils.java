package com.e4motion.common.utils;

import com.e4motion.common.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
	
	public static Response getResponse(String jsonContent) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonContent, Response.class);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	
	public static String getJsonContent(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			return "";
		}
	}
	
}

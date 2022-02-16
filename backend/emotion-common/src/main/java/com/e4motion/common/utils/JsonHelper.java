package com.e4motion.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static <T> String toJson(T object) 
    		throws JsonProcessingException {
    	
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static <T> T fromJson(String payload, Class<T> clazz) 
    		throws JsonProcessingException {
    	
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(payload, clazz);
    }

    public static <T> T fromJson(String payload, TypeReference<T> clazz) 
    		throws JsonProcessingException {
    	
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(payload, clazz);
    }

}
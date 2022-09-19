package com.e4motion.challenge.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static <T> String toJson(T object) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(object);
    }

    public static <T> T fromJson(String payload, Class<T> clazz) throws JsonProcessingException {

        return new ObjectMapper().readValue(payload, clazz);
    }

    public static <T> T fromJson(String payload, TypeReference<T> clazz) throws JsonProcessingException {

        return new ObjectMapper().readValue(payload, clazz);
    }

    public static <T> T fromJsonObject(Object object, Class<T> clazz) {

        return new ObjectMapper().convertValue(object, clazz);
    }
}
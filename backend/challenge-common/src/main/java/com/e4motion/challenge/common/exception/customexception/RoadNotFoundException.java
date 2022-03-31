package com.e4motion.challenge.common.exception.customexception;

public class RoadNotFoundException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "NONEXISTENT_ROAD";

    public static final String INVALID_ROAD_ID = "Invalid road id";

    public RoadNotFoundException(String message) {
        super(CODE, message);
    }
}

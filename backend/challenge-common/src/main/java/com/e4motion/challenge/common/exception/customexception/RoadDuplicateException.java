package com.e4motion.challenge.common.exception.customexception;

public class RoadDuplicateException extends CustomException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "ALREADY_EXISTENT_ROAD";

    public static final String ROAD_ID_ALREADY_EXISTS = "Road id already exists";

    public RoadDuplicateException(String message) {
        super(CODE, message);
    }
}

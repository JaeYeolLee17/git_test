package com.e4motion.challenge.common.exception.customexception;

public class IntersectionNotFoundException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "NONEXISTENT_INTERSECTION";

    public static final String INVALID_INTERSECTION_NO = "Invalid intersection no";

    public IntersectionNotFoundException(String message) {
        super(CODE, message);
    }
}

package com.e4motion.challenge.common.exception.customexception;

public class IntersectionDuplicateException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "ALREADY_EXISTENT_INTERSECTION";

    public static final String INTERSECTION_NO_ALREADY_EXISTS = "Intersection no already exists";

    public IntersectionDuplicateException(String message) {
        super(CODE, message);
    }
}

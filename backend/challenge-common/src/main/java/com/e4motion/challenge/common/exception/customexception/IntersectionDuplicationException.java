package com.e4motion.challenge.common.exception.customexception;

public class IntersectionDuplicationException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "ALREADY_EXISTENT_INTERSECTION";

    public static final String INTERSECTION_ID_ALREADY_EXISTS = "Intersection id already exists";

    public IntersectionDuplicationException(String message) {
        super(CODE, message);
    }
}

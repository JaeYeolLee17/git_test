package com.e4motion.challenge.common.exception.customexception;

public class RegionNotFoundException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "NONEXISTENT_REGION";

    public static final String INVALID_REGION_NO = "Invalid region no";

    public RegionNotFoundException(String message) {
        super(CODE, message);
    }
}

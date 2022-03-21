package com.e4motion.challenge.common.exception.customexception;

public class LinkNotFoundException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "NONEXISTENT_LINK";

    public static final String INVALID_LINK_ID = "Invalid link id";

    public LinkNotFoundException(String message) {
        super(CODE, message);
    }
}

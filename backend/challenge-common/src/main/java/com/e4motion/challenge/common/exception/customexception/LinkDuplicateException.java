package com.e4motion.challenge.common.exception.customexception;

public class LinkDuplicateException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "ALREADY_EXISTENT_LINK";

    public static final String LINK_START_END_ALREADY_EXISTS = "Link start, end already exists";

    public LinkDuplicateException(String message) {
        super(CODE, message);
    }
}

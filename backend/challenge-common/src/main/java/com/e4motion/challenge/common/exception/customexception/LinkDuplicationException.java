package com.e4motion.challenge.common.exception.customexception;

public class LinkDuplicationException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "ALREADY_EXISTENT_LINK";

    public static final String LINK_ID_ALREADY_EXISTS = "Link id already exists";

    public LinkDuplicationException(String message) {
        super(CODE, message);
    }
}

package com.e4motion.challenge.common.exception.customexception;

public class RegionDuplicationException extends CustomException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String CODE = "ALREADY_EXISTENT_REGION";

    public static final String REGION_ID_ALREADY_EXISTS = "Region id already exists";

    public RegionDuplicationException(String message) {
        super(CODE, message);
    }
}

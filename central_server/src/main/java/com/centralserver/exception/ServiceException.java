package com.centralserver.exception;

import com.centralserver.exception.base.SystemBaseException;

public class ServiceException extends SystemBaseException {

    public static final String SAME_PASSWORD = "same_password";
    public static final String INCORRECT_PASSWORD_RESET_DATA = "incorect_password_reset_data";
    public static final String INCORRECT_PASSWORD = "incorrect_password";
    public static final String INCORRECT_LENGTH_PASSWORD = "incorrect_length_password";


    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
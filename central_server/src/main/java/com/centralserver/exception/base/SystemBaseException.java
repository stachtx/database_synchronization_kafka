package com.centralserver.exception.base;


public class SystemBaseException extends Exception {

    protected SystemBaseException() {
    }

    protected SystemBaseException(String message) {
        super(message);
    }

    protected SystemBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
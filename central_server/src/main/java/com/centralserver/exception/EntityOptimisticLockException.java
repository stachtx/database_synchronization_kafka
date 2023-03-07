package com.centralserver.exception;

import com.centralserver.exception.base.SystemBaseException;

public class EntityOptimisticLockException extends SystemBaseException {

    public static final String OPTIMISTIC_LOCK = "error_optimistic_lock";

    public EntityOptimisticLockException() {
    }

    public EntityOptimisticLockException(String message) {
        super(message);
    }

    public EntityOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

}
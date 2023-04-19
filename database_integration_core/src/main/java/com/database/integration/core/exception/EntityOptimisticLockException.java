package com.database.integration.core.exception;

import com.database.integration.core.exception.base.SystemBaseException;

public class EntityOptimisticLockException extends SystemBaseException {

    public EntityOptimisticLockException() {
    }

    public EntityOptimisticLockException(String message) {
        super(message);
    }

    public EntityOptimisticLockException(ErrorMessage errorMessage) {
        super(errorMessage.message);
    }

    public EntityOptimisticLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public enum ErrorMessage {
        OPTIMISTIC_LOCK("error_optimistic_lock");

        public final String message;

        ErrorMessage(String message) {
            this.message = message;
        }
    }
}
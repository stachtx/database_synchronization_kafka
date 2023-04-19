package com.database.integration.core.exception;

import com.database.integration.core.exception.base.SystemBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotInDatabaseException extends SystemBaseException {


    public EntityNotInDatabaseException() {
    }

    public EntityNotInDatabaseException(String message) {
        super(message);
    }

    public EntityNotInDatabaseException(ErrorMessage errorMessage) {
        super(errorMessage.message);
    }

    public EntityNotInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }


    public enum ErrorMessage {
        NO_OBJECT("error_no_object_in_database"),
        NO_USERNAME("error_no_username_in_database");

        public final String message;

        ErrorMessage(String message) {
            this.message = message;
        }
    }
}
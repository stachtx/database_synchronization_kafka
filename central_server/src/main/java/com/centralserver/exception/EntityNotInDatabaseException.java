package com.centralserver.exception;

import com.centralserver.exception.base.SystemBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotInDatabaseException extends SystemBaseException {

    public static final String NO_OBJECT = "error_no_object_in_database";
    public static final String NO_USERNAME = "error_no_username_in_database";

    public EntityNotInDatabaseException() {
    }

    public EntityNotInDatabaseException(String message) {
        super(message);
    }

    public EntityNotInDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
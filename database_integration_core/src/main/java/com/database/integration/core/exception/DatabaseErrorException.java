package com.database.integration.core.exception;

import com.database.integration.core.exception.base.SystemBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DatabaseErrorException extends SystemBaseException {


    public DatabaseErrorException() {
    }

    public DatabaseErrorException(String message) {
        super(message);
    }

    public DatabaseErrorException(ErrorMessage errorMessage) {
        super(errorMessage.message);
    }

    public DatabaseErrorException(String message, Throwable cause) {
        super(message, cause);
    }


    public enum ErrorMessage {
      DATABASE_ERROR("error_database"),
      ILLEGAL_ARGUMENT("illegal_argument"),
      EMAIL_TAKEN("email_taken"),
      USERNAME_TAKEN("un_taken"),
      SUCCESS("success"),
      SAME_PASSWORD("same_password"),
      DEPARTMENT_NAME_TAKEN("dn_taken"),
      SERIAL_NUMBER_NAME_TAKEN("sn_taken"),
      PRODUCT_TYPE_NAME_TAKEN("dmn_taken");

      public final String message;

      ErrorMessage(String message) {
        this.message = message;
      }
    }
}
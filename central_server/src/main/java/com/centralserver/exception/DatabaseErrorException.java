package com.centralserver.exception;

import com.centralserver.exception.base.SystemBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DatabaseErrorException extends SystemBaseException {

    public static final String DATABASE_ERROR = "error_database";
    public static final String ILLEGAL_ARGUMENT = "illegal_argument";
    // User response codes
    public static final String EMAIL_TAKEN = "email_taken";
    public static final String USERNAME_TAKEN = "un_taken";
    public static final String SUCCESS = "success";
    public static final String SAME_PASSWORD = "same_password";

    //Department response codes
    public static final String DEPARTMENT_NAME_TAKEN = "dn_taken";

    //Product response codes
    public static final String SERIAL_NUMBER_NAME_TAKEN = "sn_taken";

    //Product mode response codes
    public static final String PRODUCT_TYPE_NAME_NAME_TAKEN = "dmn_taken";


    public DatabaseErrorException() {
    }

    public DatabaseErrorException(String message) {
        super(message);
    }

    public DatabaseErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
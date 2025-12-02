package com.parmida98.school_webpage.error.exception;

public class UserIsNotStudentException extends RuntimeException {

    public UserIsNotStudentException(String id) {
        super("User with id '" + id + "' is not a STUDENT");
    }
}

package com.parmida98.school_webpage.error.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String id) {
        super("Student with id '" + id + "' was not found");
    }
}

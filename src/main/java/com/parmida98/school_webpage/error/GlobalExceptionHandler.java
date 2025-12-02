package com.parmida98.school_webpage.error;

import com.parmida98.school_webpage.error.exception.StudentNotFoundException;
import com.parmida98.school_webpage.error.exception.UserIsNotStudentException;
import com.parmida98.school_webpage.error.exception.UsernameAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private DTOError buildError(HttpStatus status,
                                String message,
                                HttpServletRequest request) {

        return new DTOError(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    // Username redan upptaget
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<DTOError> handleUsernameAlreadyExists(UsernameAlreadyExistsException e, HttpServletRequest request) {

        logger.warn("Username already exists: {}", e.getMessage());

        DTOError error = new DTOError(
                OffsetDateTime.now(),
                HttpStatus.CONFLICT.value(),          // 409 – "resource already exists"
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<DTOError> handleStudentNotFound(StudentNotFoundException e,
                                                          HttpServletRequest request) {

        logger.warn("Student not found: {}", e.getMessage());

        DTOError error = buildError(
                HttpStatus.NOT_FOUND,          // 404
                e.getMessage(),
                request
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserIsNotStudentException.class)
    public ResponseEntity<DTOError> handleUserIsNotStudent(UserIsNotStudentException e,
                                                           HttpServletRequest request) {

        logger.warn("User is not a student: {}", e.getMessage());

        DTOError error = buildError(
                HttpStatus.BAD_REQUEST,        // 400
                e.getMessage(),
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DTOError> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {

        logger.warn("Bad request on {}: {}", request.getRequestURI(), e.getMessage());

        DTOError error = buildError(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Fångar allt annat
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DTOError> handleException(Exception e, HttpServletRequest request) {
        logger.error("Internal server error on {}", request.getRequestURI(), e);

        DTOError error = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

package com.parmida98.school_webpage.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Username redan upptaget / annan bad request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DTOError> handleArgumentException(IllegalArgumentException e, HttpServletRequest request) {

        log.warn("Bad request: {}", e.getMessage());

        DTOError error = new DTOError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    // Inte behöring
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DTOError> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("Access denied on {}: {}", request.getRequestURI(), e.getMessage());

        DTOError error = new DTOError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You do not have permission to access this",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Fångar allt annat
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DTOError> handleException(Exception e, HttpServletRequest request) {
        log.warn("Internal server error on {}: {}", request.getRequestURI(), e.getMessage());

        DTOError error = new DTOError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

package org.nazar.notesbackend.advice;

import org.nazar.notesbackend.entity.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

/**
 * Global exception handler that captures and handles specific exceptions across the entire application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles IllegalArgumentExceptions thrown anywhere in the application.
     * @param ex the exception that was thrown.
     * @return a ResponseEntity containing the error details.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles NoSuchElementException thrown anywhere in the application.
     * @param ex the exception that was thrown.
     * @return a ResponseEntity containing the error details.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(Exception ex) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}

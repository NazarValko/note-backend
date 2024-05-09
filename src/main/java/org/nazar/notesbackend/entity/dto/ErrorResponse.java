package org.nazar.notesbackend.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representing the error response format for exception handling.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final int status;
    private final String message;
}

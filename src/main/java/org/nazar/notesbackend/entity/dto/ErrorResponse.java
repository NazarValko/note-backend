package org.nazar.notesbackend.entity.dto;

/**
 * Class representing the error response format for exception handling.
 */
public record ErrorResponse(int status, String message) {
}

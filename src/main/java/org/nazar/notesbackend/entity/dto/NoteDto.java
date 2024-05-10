package org.nazar.notesbackend.entity.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for notes.
 */
public record NoteDto(Long id, String name, String description, LocalDate createdAt) {}

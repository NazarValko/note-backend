package org.nazar.notesbackend.controller;

import java.util.List;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.nazar.notesbackend.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing notes.
 */
@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    /**
     * Retrieves all notes.
     * @return a ResponseEntity containing a list of NoteDto.
     */
    @GetMapping
    public ResponseEntity<List<NoteDto>> getAll() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    /**
     * Retrieves a single note by its ID.
     * @param id the ID of the note to retrieve.
     * @return a ResponseEntity containing the NoteDto.
     */
    @GetMapping("{id}")
    public ResponseEntity<NoteDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    /**
     * Creates a new note.
     * @param saveRequest the NoteDto containing the details of the new note.
     * @return a ResponseEntity containing the newly created NoteDto.
     */
    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto saveRequest) {
        return ResponseEntity.ok(noteService.createNote(saveRequest));
    }

    /**
     * Updates an existing note.
     * @param updateRequest the NoteDto containing the updated details of the note.
     * @param id the ID of the note to update.
     * @return a ResponseEntity containing the updated NoteDto.
     */
    @PutMapping("{id}")
    public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto updateRequest, @PathVariable Long id) {
        return ResponseEntity.ok(noteService.updateNote(updateRequest, id));
    }

    /**
     * Deletes a note by its ID.
     * @param id the ID of the note to delete.
     * @return a ResponseEntity indicating the outcome.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        noteService.deleteById(id);
        return ResponseEntity.ok("Note with id: " + id + " was deleted successfully");
    }

}

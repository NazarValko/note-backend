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

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAll() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("{id}")
    public ResponseEntity<NoteDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto saveRequest) {
        return ResponseEntity.ok(noteService.createNote(saveRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<NoteDto> updateNote(@RequestBody NoteDto updateRequest, @PathVariable Long id) {
        return ResponseEntity.ok(noteService.updateNote(updateRequest, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        noteService.deleteById(id);
        return ResponseEntity.ok("Note with id: " + id + " was deleted successfully");
    }

}

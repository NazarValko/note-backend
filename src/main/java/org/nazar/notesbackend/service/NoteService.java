package org.nazar.notesbackend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.repository.NotesRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private final NotesRepository notesRepository;

    public NoteService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public Note getNoteById(Long id) {
        return notesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find note with such id: " + id));
    }

    public List<Note> getAllNotes() {
        return notesRepository.findAll();
    }

    public Note createNote(Note request) {
        if (notesRepository.existsNoteByName(request.getName())) {
            throw new IllegalArgumentException("Note with such name: " + request.getName() + " already exists");
        }
        request.setCreatedAt(LocalDate.now());
        return notesRepository.save(request);
    }

    public Note updateNote(Note newNote, Long id) {
        Note noteToUpdate = notesRepository.findNoteById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find note with such id: " + id));

        Optional.ofNullable(newNote.getName()).ifPresent(noteToUpdate::setName);
        Optional.ofNullable(newNote.getDescription()).ifPresent(noteToUpdate::setDescription);

        return notesRepository.save(noteToUpdate);
    }

    public void deleteById(Long id) {
        if (!notesRepository.existsNoteById(id)) {
            throw new IllegalArgumentException("Cannot find note with such id: " + id);
        }
        notesRepository.deleteById(id);
    }
}

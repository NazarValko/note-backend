package org.nazar.notesbackend.service;

import java.util.List;
import java.util.Optional;
import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.nazar.notesbackend.repository.NotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {
    private final NotesRepository notesRepository;

    public NoteService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public NoteDto getNoteById(Long id) {
        return notesRepository.mapToDto(
                notesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find note with such id: " + id)));
    }

    public List<NoteDto> getAllNotes() {
        return notesRepository.findAll().stream().map(notesRepository::mapToDto).toList();
    }

    @Transactional
    public NoteDto createNote(NoteDto request) {
        if (notesRepository.existsNoteByName(request.getName())) {
            throw new IllegalArgumentException("Note with such name: " + request.getName() + " already exists");
        }

        Note noteToBeSaved = notesRepository.mapToEntity(request);

        return notesRepository.mapToDto(notesRepository.save(noteToBeSaved));
    }

    @Transactional
    public NoteDto updateNote(NoteDto newNote, Long id) {
        Note noteToUpdate = notesRepository.findNoteById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find note with such id: " + id));

        Optional.ofNullable(newNote.getName()).ifPresent(noteToUpdate::setName);
        Optional.ofNullable(newNote.getDescription()).ifPresent(noteToUpdate::setDescription);

        return notesRepository.mapToDto(notesRepository.save(noteToUpdate));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!notesRepository.existsNoteById(id)) {
            throw new IllegalArgumentException("Cannot find note with such id: " + id);
        }
        notesRepository.deleteById(id);
    }
}

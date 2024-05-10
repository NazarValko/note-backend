package org.nazar.notesbackend.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.nazar.notesbackend.mapper.NoteMapper;
import org.nazar.notesbackend.repository.NotesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing notes.
 */
@Service
public class NoteService {
    private final NotesRepository notesRepository;

    private final NoteMapper noteMapper;

    public NoteService(NotesRepository notesRepository, NoteMapper noteMapper) {
        this.notesRepository = notesRepository;
        this.noteMapper = noteMapper;
    }

    /**
     * Retrieves a note by its ID.
     * @param id the ID of the note to retrieve.
     * @return the NoteDto of the retrieved note.
     */
    @Transactional(readOnly = true)
    public NoteDto getNoteById(Long id) {
        return noteMapper.mapToDto(
                notesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find note with such id: " + id)));
    }

    /**
     * Retrieves all notes.
     * @return a List of NoteDto representing all notes.
     */
    @Transactional(readOnly = true)
    public List<NoteDto> getAllNotes() {
        return notesRepository.findAll().stream().map(noteMapper::mapToDto).toList();
    }

    /**
     * Creates a new note.
     * @param request the NoteDto containing the note details.
     * @return the NoteDto of the newly created note.
     */
    @Transactional
    public NoteDto createNote(NoteDto request) {
        if (notesRepository.existsNoteByName(request.name())) {
            throw new IllegalArgumentException("Note with such name: " + request.name() + " already exists");
        }

        Note noteToBeSaved = noteMapper.mapToEntity(request);

        return noteMapper.mapToDto(notesRepository.save(noteToBeSaved));
    }

    /**
     * Updates an existing note.
     * @param newNote the NoteDto containing the updated details.
     * @param id the ID of the note to update.
     * @return the NoteDto of the updated note.
     */
    @Transactional
    public NoteDto updateNote(NoteDto newNote, Long id) {
        Note noteToUpdate = notesRepository.findNoteById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find note with such id: " + id));

        Optional.ofNullable(newNote.name()).ifPresent(noteToUpdate::setName);
        Optional.ofNullable(newNote.description()).ifPresent(noteToUpdate::setDescription);

        return noteMapper.mapToDto(notesRepository.save(noteToUpdate));
    }

    /**
     * Deletes a note by its ID.
     * @param id the ID of the note to delete.
     */
    @Transactional
    public void deleteById(Long id) {
        if (!notesRepository.existsNoteById(id)) {
            throw new NoSuchElementException("Cannot find note with such id: " + id);
        }
        notesRepository.deleteById(id);
    }
}

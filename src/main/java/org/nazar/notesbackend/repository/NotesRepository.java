package org.nazar.notesbackend.repository;

import java.util.Optional;
import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Note entity.
 */
@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {
    Optional<Note> findNoteById(Long id);

    boolean existsNoteByName(String name);

    boolean existsNoteById(Long id);

    default Note mapToEntity(NoteDto noteDto) {
        Note note = new Note();
        note.setName(noteDto.getName());
        note.setDescription(noteDto.getDescription());
        return note;
    }
    default NoteDto mapToDto(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setName(note.getName());
        noteDto.setDescription(note.getDescription());
        noteDto.setCreatedAt(note.getCreatedAt());
        return noteDto;
    }
}

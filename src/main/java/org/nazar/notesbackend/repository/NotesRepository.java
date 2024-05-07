package org.nazar.notesbackend.repository;

import java.util.Optional;
import org.nazar.notesbackend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {
    Optional<Note> findNoteById(Long id);

    boolean existsNoteByName(String name);

    boolean existsNoteById(Long id);
}

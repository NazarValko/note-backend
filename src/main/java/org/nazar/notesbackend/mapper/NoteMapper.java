package org.nazar.notesbackend.mapper;

import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {
    public Note mapToEntity(NoteDto noteDto) {
        Note note = new Note();
        note.setName(noteDto.name());
        note.setDescription(noteDto.description());
        return note;
    }

    public NoteDto mapToDto(Note note) {
        return new NoteDto(note.getId(), note.getName(), note.getDescription(), note.getCreatedAt());
    }
}

package org.nazar.notesbackend.service;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.nazar.notesbackend.repository.NotesRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {
    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NoteService noteService;

    private NoteDto noteDto;
    private Note note;

    @BeforeEach
    void setUp() {
        noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setName("Sample Note");
        noteDto.setDescription("This is a sample note.");

        note = new Note();
        note.setId(noteDto.getId());
        note.setName(noteDto.getName());
        note.setDescription(noteDto.getDescription());
    }
    @Test
    void testGetAllNotes_WhenNotesExist_ThenReturnNoteList() {
        when(notesRepository.findAll()).thenReturn(List.of(note));
        when(notesRepository.mapToDto(any(Note.class))).thenReturn(noteDto);

        List<NoteDto> noteDtos = noteService.getAllNotes();

        assertEquals(1, noteDtos.size());
        assertEquals("Sample Note", noteDtos.getFirst().getName());
    }
    @Test
    void testGetNoteById_WhenNoteExists_ThenReturnNoteDto() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        when(notesRepository.mapToDto(any(Note.class))).thenReturn(noteDto);

        NoteDto foundNote = noteService.getNoteById(1L);

        assertNotNull(foundNote);
        assertEquals("Sample Note", foundNote.getName());
    }

    @Test
    void testGetNoteById_WhenNoteDoesNotExist_ThenThrowException() {
        when(notesRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.getNoteById(1L));

        assertEquals("Cannot find note with such id: 1", exception.getMessage());
    }

    @Test
    void testCreateNote_WhenNameIsUnique_ThenReturnSavedNote() {
        when(notesRepository.mapToEntity(noteDto)).thenReturn(note);
        when(notesRepository.existsNoteByName(noteDto.getName())).thenReturn(false);
        when(notesRepository.save(any(Note.class))).thenReturn(note);
        when(notesRepository.mapToDto(any(Note.class))).thenReturn(noteDto);

        NoteDto savedNote = noteService.createNote(noteDto);

        assertNotNull(savedNote);
        assertEquals("Sample Note", savedNote.getName());
    }

    @Test
    void testCreateNote_WhenNameAlreadyExists_ThenThrowException() {
        when(notesRepository.existsNoteByName(noteDto.getName())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.createNote(noteDto));

        assertEquals("Note with such name: Sample Note already exists", exception.getMessage());
    }

    @Test
    void testUpdateNote_WhenNoteExists_ThenReturnUpdatedNote() {
        when(notesRepository.findNoteById(1L)).thenReturn(Optional.of(note));
        when(notesRepository.save(any(Note.class))).thenReturn(note);
        when(notesRepository.mapToDto(any(Note.class))).thenReturn(noteDto);

        NoteDto updatedNote = noteService.updateNote(noteDto, 1L);

        assertNotNull(updatedNote);
        assertEquals("Sample Note", updatedNote.getName());
    }

    @Test
    void testUpdateNote_WhenNoteDoesNotExist_ThenThrowException() {
        when(notesRepository.findNoteById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.updateNote(noteDto, 1L));

        assertEquals("Cannot find note with such id: 1", exception.getMessage());
    }

    @Test
    void testDeleteById_WhenNoteExists_ThenSuccess() {
        when(notesRepository.existsNoteById(1L)).thenReturn(true);
        doNothing().when(notesRepository).deleteById(1L);

        assertDoesNotThrow(() -> noteService.deleteById(1L));
    }

    @Test
    void testDeleteById_WhenNoteDoesNotExist_ThenThrowException() {
        when(notesRepository.existsNoteById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.deleteById(1L));

        assertEquals("Cannot find note with such id: 1", exception.getMessage());
    }
}

package org.nazar.notesbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nazar.notesbackend.entity.Note;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.nazar.notesbackend.mapper.NoteMapper;
import org.nazar.notesbackend.repository.NotesRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the NoteService class using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
    @Mock
    private NotesRepository notesRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    private NoteDto noteDto;

    private Note note;

    /**
     * Set up common test objects and configurations.
     */
    @BeforeEach
    void setUp() {
        noteDto = new NoteDto(1L, "Sample Note", "This is a sample note.", null);

        note = new Note();
        note.setId(noteDto.id());
        note.setName(noteDto.name());
        note.setDescription(noteDto.description());
    }

    /**
     * Test to verify that getAllNotes returns a list of notes when they exist.
     */
    @Test
    void testGetAllNotes_WhenNotesExist_ThenReturnNoteList() {
        when(notesRepository.findAll()).thenReturn(List.of(note));
        when(noteMapper.mapToDto(any(Note.class))).thenReturn(noteDto);

        List<NoteDto> noteDtos = noteService.getAllNotes();

        assertEquals(1, noteDtos.size());
        assertEquals("Sample Note", noteDtos.getFirst().name());
    }

    /**
     * Test to verify that getNoteById returns the correct note DTO when the note exists.
     */
    @Test
    void testGetNoteById_WhenNoteExists_ThenReturnNoteDto() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteMapper.mapToDto(any(Note.class))).thenReturn(noteDto);

        NoteDto foundNote = noteService.getNoteById(1L);

        assertNotNull(foundNote);
        assertEquals("Sample Note", foundNote.name());
    }

    /**
     * Test to verify that getNoteById throws an exception when the note does not exist.
     */
    @Test
    void testGetNoteById_WhenNoteDoesNotExist_ThenThrowException() {
        when(notesRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.getNoteById(1L));

        assertEquals("Cannot find note with such id: 1", exception.getMessage());
    }

    /**
     * Test to verify that createNote returns the saved note DTO when the note name is unique.
     */
    @Test
    void testCreateNote_WhenNameIsUnique_ThenReturnSavedNote() {
        when(noteMapper.mapToEntity(noteDto)).thenReturn(note);
        when(notesRepository.existsNoteByName(noteDto.name())).thenReturn(false);
        when(notesRepository.save(any(Note.class))).thenReturn(note);
        when(noteMapper.mapToDto(any(Note.class))).thenReturn(noteDto);

        NoteDto savedNote = noteService.createNote(noteDto);

        assertNotNull(savedNote);
        assertEquals("Sample Note", savedNote.name());
    }

    /**
     * Test to verify that createNote throws an exception when the note name already exists.
     */
    @Test
    void testCreateNote_WhenNameAlreadyExists_ThenThrowException() {
        when(notesRepository.existsNoteByName(noteDto.name())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.createNote(noteDto));

        assertEquals("Note with such name: Sample Note already exists", exception.getMessage());
    }

    /**
     * Test to verify that updateNote returns the updated note DTO when the original note exists.
     */
    @Test
    void testUpdateNote_WhenNoteExists_ThenReturnUpdatedNote() {
        when(notesRepository.findNoteById(1L)).thenReturn(Optional.of(note));
        when(notesRepository.save(any(Note.class))).thenReturn(note);
        when(noteMapper.mapToDto(any(Note.class))).thenReturn(noteDto);

        NoteDto updatedNote = noteService.updateNote(noteDto, 1L);

        assertNotNull(updatedNote);
        assertEquals("Sample Note", updatedNote.name());
    }

    /**
     * Test to verify that updateNote throws an exception when the note does not exist.
     */
    @Test
    void testUpdateNote_WhenNoteDoesNotExist_ThenThrowException() {
        when(notesRepository.findNoteById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> noteService.updateNote(noteDto, 1L));

        assertEquals("Cannot find note with such id: 1", exception.getMessage());
    }

    /**
     * Test to verify that deleteById completes without throwing an exception when the note exists.
     */
    @Test
    void testDeleteById_WhenNoteExists_ThenSuccess() {
        when(notesRepository.existsNoteById(1L)).thenReturn(true);
        doNothing().when(notesRepository).deleteById(1L);

        assertDoesNotThrow(() -> noteService.deleteById(1L));
    }

    /**
     * Test to verify that deleteById throws an exception when the note does not exist.
     */
    @Test
    void testDeleteById_WhenNoteDoesNotExist_ThenThrowException() {
        when(notesRepository.existsNoteById(anyLong())).thenReturn(false);

        Exception exception = assertThrows(NoSuchElementException.class, () -> noteService.deleteById(1L));

        assertEquals("Cannot find note with such id: 1", exception.getMessage());
    }
}

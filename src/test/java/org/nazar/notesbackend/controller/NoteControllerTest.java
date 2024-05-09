package org.nazar.notesbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.nazar.notesbackend.entity.dto.NoteDto;
import org.nazar.notesbackend.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Spring MVC test for the NoteController class.
 */
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    /**
     * Helper method to convert an object into JSON string.
     * @param obj The object to be converted to JSON.
     * @return JSON string representation of the object.
     */
    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test to verify that the getAllNotes method returns all notes when notes exist.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testGetAllNotes_WhenNotesExist_ThenReturnNotes() throws Exception {
        LocalDate today = LocalDate.now();
        List<NoteDto> notes = List.of(
                new NoteDto(1L, "Test Note", "This is a test note", today)
        );
        when(noteService.getAllNotes()).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Note"))
                .andExpect(jsonPath("$[0].description").value("This is a test note"))
                .andExpect(jsonPath("$[0].createdAt").value(today.toString()));
    }

    /**
     * Test to verify that the getAllNotes method returns an empty list when no notes exist.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testGetAllNotes_WhenNoNotesExist_ThenReturnEmpty() throws Exception {
        when(noteService.getAllNotes()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /**
     * Test to verify that the getNoteById method returns the note when the note exists.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testGetNoteById_WhenNoteExists_ThenReturnNote() throws Exception {
        LocalDate today = LocalDate.now();
        NoteDto note = new NoteDto(1L, "Test Note", "This is a test note", today);
        when(noteService.getNoteById(1L)).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Note"))
                .andExpect(jsonPath("$.description").value("This is a test note"))
                .andExpect(jsonPath("$.createdAt").value(today.toString()));
    }

    /**
     * Test to verify that the getNoteById method returns a BadRequest when the note does not exist.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testGetNoteById_WhenNoteDoesNotExist_ThenBadRequest() throws Exception {
        when(noteService.getNoteById(1L)).thenThrow(new IllegalArgumentException("Cannot find note with such id: 1"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notes/1"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test to verify that the createNote method returns the created note.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testCreateNote_WhenNoteIsCreated_ThenReturnCreatedNote() throws Exception {
        LocalDate today = LocalDate.now();
        NoteDto newNote = new NoteDto(2L, "New Note", "This is a new note", today);

        when(noteService.createNote(any(NoteDto.class))).thenReturn(newNote);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New Note"))
                .andExpect(jsonPath("$.description").value("This is a new note"))
                .andExpect(jsonPath("$.createdAt").value(today.toString()));
    }

    /**
     * Test to verify that the createNote method returns a BadRequest when the note name already exists.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testCreateNote_WhenNameExists_ThenBadRequest() throws Exception {
        LocalDate today = LocalDate.now();
        NoteDto newNote = new NoteDto(null, "Existing Note", "This is a duplicate note", today);

        when(noteService.createNote(any(NoteDto.class)))
                .thenThrow(new IllegalArgumentException("Note with such name: " + newNote.getName() + " already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newNote)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test to verify that the updateNote method returns the updated note when it exists.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testUpdateNote_WhenNoteExists_ThenReturnUpdatedNote() throws Exception {
        LocalDate today = LocalDate.now();
        NoteDto updatedNote = new NoteDto(1L, "Updated Name", "Updated Description", today);

        when(noteService.updateNote(any(NoteDto.class), eq(1L))).thenReturn(updatedNote);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    /**
     * Test to verify that the updateNote method returns a BadRequest when the note does not exist.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testUpdateNote_WhenNoteDoesNotExist_ThenBadRequest() throws Exception {
        when(noteService.updateNote(any(NoteDto.class), eq(1L))).thenThrow(new IllegalArgumentException("Cannot find note with such id: 1"));

        NoteDto updatedNote = new NoteDto(1L, "Updated Name", "Updated Description", null);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedNote)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test to verify that the deleteNote method returns a success message when the note exists.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testDeleteNote_WhenNoteExists_ThenReturnSuccessMessage() throws Exception {
        doNothing().when(noteService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/notes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Note with id: 1 was deleted successfully"));
    }

    /**
     * Test to verify that the deleteNote method returns a BadRequest when the note does not exist.
     * @throws Exception when mockMvc perform throws an exception.
     */
    @Test
    void testDeleteNote_WhenNoteDoesNotExist_ThenBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Cannot find note with such id: 1")).when(noteService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/notes/1"))
                .andExpect(status().isBadRequest());
    }
}

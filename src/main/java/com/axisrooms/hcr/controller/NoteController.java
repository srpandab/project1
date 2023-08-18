package com.axisrooms.hcr.controller;
import com.axisrooms.hcr.dto.Note;
import com.axisrooms.hcr.exception.ResourceNotFoundException;
import com.axisrooms.hcr.payload.response.MessageResponse;
import com.axisrooms.hcr.repository.NoteRepository;
import com.axisrooms.hcr.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;
    @Autowired
    NoteService noteService;

    // Get All Notes
    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // Create a new Note
    @PostMapping
    public ResponseEntity<?> createNote(@Validated  @RequestBody Note note) {
        return noteService.saveAndUpdateNote(note,null);
    }

    // Get a Single Note
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable(value = "id") String noteId) {
        return noteRepository.findById(noteId).orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    // Update a Note
    @PutMapping
    public ResponseEntity<?> updateNote(@RequestParam String noteId, @Validated @RequestBody Note noteDetails) {
        return  noteService.saveAndUpdateNote(noteDetails,noteId);
    }

    // Delete a Note
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") String noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
        noteRepository.delete(note);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setStatus("success");
        messageResponse.setMessage("Note successfully Deleted");
        return ResponseEntity.ok().body(messageResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getNoteByUserId(@RequestParam String userId) {
        return noteService.getNoteByUserId(userId);
    }
}

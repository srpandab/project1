package com.axisrooms.hcr.service;

import com.axisrooms.hcr.dto.Note;
import com.axisrooms.hcr.payload.response.MessageResponse;
import com.axisrooms.hcr.repository.NoteRepository;
import com.axisrooms.hcr.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    public ResponseEntity<?> saveAndUpdateNote(Note note, String noteId) {
        MessageResponse messageResponse = new MessageResponse();
        if (Utils.isPresent(noteId)) {
            Optional<Note> optionalNote = noteRepository.findById(noteId);
            if(optionalNote.isPresent()) {
                Note exestingNote = optionalNote.get();
                updateNote(note, messageResponse, exestingNote);
            }else {
                messageResponse.setStatus("failed");
                messageResponse.setMessage("Note not found");
            }
        } else {
            saveNewNote(note, messageResponse);
        }
        return ResponseEntity.ok().body(messageResponse);
    }

    private void updateNote(Note note, MessageResponse messageResponse, Note exestingNote) {
        exestingNote.setContent(note.getContent());
        exestingNote.setUpdatedAt(new DateTime().toString());
        noteRepository.save(exestingNote);
        messageResponse.setStatus("success");
        messageResponse.setMessage("Note successfully Updated");
    }

    private void saveNewNote(Note note, MessageResponse messageResponse) {
        note.setCreatedAt(new DateTime().toString());
        note.setUpdatedAt(new DateTime().toString());
        noteRepository.save(note);
        messageResponse.setStatus("success");
        messageResponse.setMessage("Note successfully Saved");
    }

    public ResponseEntity<?> getNoteByUserId(String userId) {
        MessageResponse messageResponse = new MessageResponse();
        if (Utils.isPresent(userId)) {
            List<Note> exestingNote = noteRepository.findByUserIdOrderByCreatedAtDesc(userId);
            if (Utils.isPresent(exestingNote)) {
                return ResponseEntity.ok().body(exestingNote);
            } else {
                messageResponse.setStatus("success");
                messageResponse.setMessage("No Message found");
            }
        } else {
            messageResponse.setStatus("success");
            messageResponse.setMessage("userId not found");
        }
        return ResponseEntity.ok().body(messageResponse);
    }
}

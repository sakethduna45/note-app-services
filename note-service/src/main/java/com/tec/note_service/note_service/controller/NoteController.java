package com.tec.note_service.note_service.controller;

import com.tec.note_service.note_service.model.Note;
import com.tec.note_service.note_service.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;



    private String getUsernameFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return "jwtTokenServiceMGGGG.extractUserNameFromApiCallToAuthService(token)";
    }

    @GetMapping
    public List<Note> getAllNotes(HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        return noteService.getUserNotes(username);
    }

    @PostMapping("/create")
    public Note createNote(@RequestBody Note note, HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        return noteService.createNote(note, username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note, HttpServletRequest request) {
        String username = getUsernameFromRequest(request);
        note.setId(id);
        return ResponseEntity.ok(noteService.updateNote(note, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.ok("Deleted");
    }
}

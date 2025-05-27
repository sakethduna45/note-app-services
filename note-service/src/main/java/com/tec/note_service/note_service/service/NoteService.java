package com.tec.note_service.note_service.service;

import com.tec.note_service.note_service.model.Note;
import com.tec.note_service.note_service.repo.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getUserNotes(String username) {
        return noteRepository.findByUsername(username);
    }

    public Note createNote(Note note, String username) {
        note.setUsername(username);
        return noteRepository.save(note);
    }

    public Optional<Note> getNoteById(Long noteId) {
        return noteRepository.findById(noteId);
    }

    public Note updateNote(Note note, String username) {
        note.setUsername(username); // ensure ownership
        return noteRepository.save(note);
    }

    public void deleteNoteById(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}


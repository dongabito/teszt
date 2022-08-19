package com.mg.teszt.services;

import com.mg.teszt.models.Note;
import java.util.List;

public interface NoteService {
  void addNote(Note note);

  List<Note> getNotes();
}

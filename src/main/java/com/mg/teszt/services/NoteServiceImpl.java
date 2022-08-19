package com.mg.teszt.services;

import com.mg.teszt.models.Note;
import com.mg.teszt.repositories.NoteRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

  private NoteRepository repository;


  @Override
  public void addNote(Note note) {
    if (repository.existsBySzemelyId(note.getSzemelyId())) {
      repository.deleteNoteByDatumTolAfterAndDatumIgBefore(note.getDatumTol(), note.getDatumIg(),
          note.getSzemelyId());
      setIntervalDate(note);
      repository.save(note);
    } else {
      repository.save(note);
    }
  }

  @Transactional
  void setIntervalDate(Note note) {
    Optional<Note> notePrev = repository.findPreviousNote(note.getSzemelyId(), note.getDatumTol());
    Optional<Note> noteNext = repository.findNextNote(note.getSzemelyId(), note.getDatumIg());
    if (noteNext.isPresent() && Objects.equals(notePrev.get(), noteNext.orElse(null))) {
      Note noteToInsert = new Note(noteNext.get());
      noteToInsert.setDatumTol(note.getDatumIg().plusDays(1L));
      noteToInsert.setTestsulyKg(note.getTestsulyKg());
      repository.save(noteToInsert);
      notePrev.ifPresent(value1 -> value1.setDatumIg(note.getDatumTol().minusDays(1L)));
    } else {
      notePrev.ifPresent(value1 -> value1.setDatumIg(note.getDatumTol().minusDays(1L)));
      noteNext.ifPresent(value2 -> value2.setDatumTol(note.getDatumIg().plusDays(1L)));
    }
  }


  @Override
  public List<Note> getNotes() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .sorted(Comparator.comparing(Note::getSzemelyId).thenComparing(Note::getDatumTol))
        .collect(Collectors.toList());
  }
}

package com.mg.teszt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mg.teszt.models.Note;
import com.mg.teszt.repositories.NoteRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Method should ")
@ExtendWith(MockitoExtension.class)
class NoteServiceImplTest {

  @Mock
  NoteRepository repository;

  @InjectMocks
  NoteServiceImpl service;

  List<Note> notes;

  @BeforeEach
  void setUp() {
    notes = Arrays.asList(
        new Note(1, 25, LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 2, 1), 30),
        new Note(2, 25, LocalDate.of(2022, 2, 2),
            LocalDate.of(2022, 3, 1), 35),
        new Note(3, 25, LocalDate.of(2022, 3, 2),
            LocalDate.of(2022, 4, 1), 40),
        new Note(4, 2, LocalDate.of(2022, 1, 1),
            LocalDate.of(2022, 2, 1), 30)
    );
  }

  @Nested
  @DisplayName("by seeking note(s) ")
  class QueryMethod {

    @Test
    @DisplayName("return list of notes in the given order")
    void testIfMethodReturnListOfNotes() {
      when(repository.findAll()).thenReturn(notes);

      List<Note> notes = StreamSupport.stream(repository.findAll().spliterator(), false)
          .sorted(Comparator.comparing(Note::getSzemelyId).thenComparing(Note::getDatumTol))
          .collect(Collectors.toList());
      assertThat(notes).hasSize(4);
      assertThat(notes.get(0)).extracting(Note::getSzemelyId).isEqualTo(2);
      assertThat(notes.get(1)).extracting(Note::getId).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("by adding note(s) ")
  class AddingMethod {

    @Test
    @DisplayName("check if the given szemely_id exists")
    void testIfSzemely_idExists() {
      service.addNote(notes.get(0));
      verify(repository).existsBySzemelyId(anyInt());
    }

    @Test
    @DisplayName("delete unnecessary interval")
    void testIfMethodDeletesUnnecessaryInterval() {
      when(repository.existsBySzemelyId(anyInt())).thenReturn(true);
      service.addNote(notes.get(0));
      verify(repository).deleteNoteByDatumTolAfterAndDatumIgBefore(any(LocalDate.class),
          any(LocalDate.class), anyInt());
    }

    @Test
    @DisplayName("save note")
    void testIfMethodSavesNote() {
      when(repository.existsBySzemelyId(anyInt())).thenReturn(true);
      service.addNote(notes.get(0));
      verify(repository).save(argThat(n -> n.getSzemelyId() == 25));
    }

    @Test
    @DisplayName("set dates of other interval")
    void testIfMethodSetsInterval() {
      Note noteUnderTest = new Note(10, 25, LocalDate.of(2022, 1, 1),
          LocalDate.of(2022, 2, 2), 30);
      when(repository.findPreviousNote(anyInt(), any(LocalDate.class))).thenReturn(
          Optional.of(noteUnderTest));
      service.setIntervalDate(notes.get(1));
      verify(repository).findPreviousNote(notes.get(1).getSzemelyId(), notes.get(1).getDatumTol());
      assertThat(noteUnderTest.getDatumIg()).isEqualTo(LocalDate.of(2022, 2, 1));
    }
  }

}
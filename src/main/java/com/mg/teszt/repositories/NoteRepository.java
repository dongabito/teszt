package com.mg.teszt.repositories;

import com.mg.teszt.models.Note;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NoteRepository extends JpaRepository<Note, Integer> {

  boolean existsBySzemelyId(Integer szemelyId);

  @Query(value = "SELECT * FROM notes n where szemely_id = ?1 AND datum_tol < ?2 ORDER BY datum_tol DESC LIMIT 1 ", nativeQuery = true)
  Optional<Note> findPreviousNote(Integer szemelyId, LocalDate datumTol);

  @Query(value = "SELECT * FROM notes n where szemely_id = ?1 AND datum_ig > ?2 ORDER BY datum_ig LIMIT 1 ", nativeQuery = true)
  Optional<Note> findNextNote(Integer szemelyId, LocalDate datumIg);

  @Transactional
  @Modifying
  @Query("delete from notes n where n.datumTol > ?1 and n.datumIg < ?2 and n.szemelyId = ?3")
  void deleteNoteByDatumTolAfterAndDatumIgBefore(LocalDate datumTol, LocalDate datumIg,
                                                 Integer szemelyId);

}

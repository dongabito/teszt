package com.mg.teszt.models;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer szemelyId;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate datumTol;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate datumIg;

  private Integer testsulyKg;

  public Note(Note note) {
    this.szemelyId = note.getSzemelyId();
    this.datumTol = note.getDatumTol();
    this.datumIg = note.getDatumIg();
    this.testsulyKg = note.getTestsulyKg();
  }
}

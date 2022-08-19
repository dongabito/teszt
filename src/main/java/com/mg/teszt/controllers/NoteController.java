package com.mg.teszt.controllers;


import com.mg.teszt.models.Note;
import com.mg.teszt.services.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/notes")
public class NoteController {

  private NoteService service;

  @GetMapping
  public String renderHomePage(Model model) {
    model.addAttribute("notes", service.getNotes());
    return "index";
  }

  @PostMapping
  public String saveNote(@ModelAttribute Note note) {
    service.addNote(note);
    return "redirect:/notes";
  }

}

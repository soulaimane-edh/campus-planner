package com.example.campusplanner.controller;

import com.example.campusplanner.model.Teacher;
import com.example.campusplanner.repository.TeacherRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());
        return "teachers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teachers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teachers/form";
        }
        teacherRepository.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow();
        model.addAttribute("teacher", teacher);
        return "teachers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("teacher") Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "teachers/form";
        }
        teacher.setId(id);
        teacherRepository.save(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        teacherRepository.deleteById(id);
        return "redirect:/teachers";
    }
}

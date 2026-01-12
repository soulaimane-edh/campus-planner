package com.example.campusplanner.controller;

import com.example.campusplanner.model.GroupClass;
import com.example.campusplanner.model.Student;
import com.example.campusplanner.repository.GroupClassRepository;
import com.example.campusplanner.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final GroupClassRepository groupClassRepository;

    public StudentController(StudentRepository studentRepository, GroupClassRepository groupClassRepository) {
        this.studentRepository = studentRepository;
        this.groupClassRepository = groupClassRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "students/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupClassRepository.findAll());
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult,
                         @RequestParam(value = "groupId", required = false) Long groupId,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupClassRepository.findAll());
            return "students/form";
        }
        if (groupId != null) {
            groupClassRepository.findById(groupId).ifPresent(student::setGroupClass);
        }
        studentRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        model.addAttribute("groups", groupClassRepository.findAll());
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("student") Student student,
                         BindingResult bindingResult,
                         @RequestParam(value = "groupId", required = false) Long groupId,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("groups", groupClassRepository.findAll());
            return "students/form";
        }
        if (groupId != null) {
            groupClassRepository.findById(groupId).ifPresent(student::setGroupClass);
        } else {
            student.setGroupClass(null);
        }
        student.setId(id);
        studentRepository.save(student);
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }
}

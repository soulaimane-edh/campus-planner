package com.example.campusplanner.controller;

import com.example.campusplanner.model.ModuleCourse;
import com.example.campusplanner.repository.GroupClassRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.TeacherRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/modules")
public class ModuleCourseController {

    private final ModuleCourseRepository moduleCourseRepository;
    private final TeacherRepository teacherRepository;
    private final GroupClassRepository groupClassRepository;

    public ModuleCourseController(ModuleCourseRepository moduleCourseRepository, TeacherRepository teacherRepository, GroupClassRepository groupClassRepository) {
        this.moduleCourseRepository = moduleCourseRepository;
        this.teacherRepository = teacherRepository;
        this.groupClassRepository = groupClassRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("modules", moduleCourseRepository.findAll());
        return "modules/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("module", new ModuleCourse());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("groups", groupClassRepository.findAll());
        return "modules/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("module") ModuleCourse moduleCourse, BindingResult bindingResult,
                         @RequestParam(value = "teacherId", required = false) Long teacherId,
                         @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherRepository.findAll());
            model.addAttribute("groups", groupClassRepository.findAll());
            return "modules/form";
        }
        if (teacherId != null) {
            teacherRepository.findById(teacherId).ifPresent(moduleCourse::setResponsibleTeacher);
        } else {
            moduleCourse.setResponsibleTeacher(null);
        }
        moduleCourse.getGroups().clear();
        if (groupIds != null) {
            moduleCourse.getGroups().addAll(groupClassRepository.findAllById(groupIds));
        }
        moduleCourseRepository.save(moduleCourse);
        return "redirect:/modules";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ModuleCourse moduleCourse = moduleCourseRepository.findById(id).orElseThrow();
        model.addAttribute("module", moduleCourse);
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("groups", groupClassRepository.findAll());
        return "modules/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("module") ModuleCourse moduleCourse,
                         BindingResult bindingResult,
                         @RequestParam(value = "teacherId", required = false) Long teacherId,
                         @RequestParam(value = "groupIds", required = false) List<Long> groupIds,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("teachers", teacherRepository.findAll());
            model.addAttribute("groups", groupClassRepository.findAll());
            return "modules/form";
        }
        moduleCourse.setId(id);
        if (teacherId != null) {
            teacherRepository.findById(teacherId).ifPresent(moduleCourse::setResponsibleTeacher);
        } else {
            moduleCourse.setResponsibleTeacher(null);
        }
        moduleCourse.getGroups().clear();
        if (groupIds != null) {
            moduleCourse.getGroups().addAll(groupClassRepository.findAllById(groupIds));
        }
        moduleCourseRepository.save(moduleCourse);
        return "redirect:/modules";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        moduleCourseRepository.deleteById(id);
        return "redirect:/modules";
    }
}

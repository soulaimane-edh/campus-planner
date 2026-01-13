package com.example.campusplanner.controller;

import com.example.campusplanner.model.ModuleCourse;
import com.example.campusplanner.model.Role;
import com.example.campusplanner.model.Student;
import com.example.campusplanner.model.Teacher;
import com.example.campusplanner.model.UserAccount;
import com.example.campusplanner.repository.GroupClassRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.StudentRepository;
import com.example.campusplanner.repository.TeacherRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Collections;
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
    private final StudentRepository studentRepository;

    public ModuleCourseController(ModuleCourseRepository moduleCourseRepository, TeacherRepository teacherRepository, GroupClassRepository groupClassRepository, StudentRepository studentRepository) {
        this.moduleCourseRepository = moduleCourseRepository;
        this.teacherRepository = teacherRepository;
        this.groupClassRepository = groupClassRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("modules", moduleCourseRepository.findAll());
        return "modules/list";
    }

    @GetMapping("/my")
    public String myModules(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (currentUser.getRole() == Role.TEACHER) {
            Teacher teacher = teacherRepository.findByEmail(currentUser.getEmail()).orElse(null);
            model.addAttribute("modules", teacher == null
                    ? Collections.emptyList()
                    : moduleCourseRepository.findByResponsibleTeacherId(teacher.getId()));
            model.addAttribute("profileMissing", teacher == null);
            return "modules/my";
        }
        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByEmail(currentUser.getEmail()).orElse(null);
            model.addAttribute("modules", student == null || student.getGroupClass() == null
                    ? Collections.emptySet()
                    : student.getGroupClass().getModules());
            model.addAttribute("profileMissing", student == null || student.getGroupClass() == null);
            return "modules/my";
        }
        return "redirect:/modules";
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

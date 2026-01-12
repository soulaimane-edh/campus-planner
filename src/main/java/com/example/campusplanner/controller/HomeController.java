package com.example.campusplanner.controller;

import com.example.campusplanner.repository.GroupClassRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.SessionSlotRepository;
import com.example.campusplanner.repository.StudentRepository;
import com.example.campusplanner.repository.TeacherRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ModuleCourseRepository moduleCourseRepository;
    private final SessionSlotRepository sessionSlotRepository;
    private final GroupClassRepository groupClassRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public HomeController(ModuleCourseRepository moduleCourseRepository,
                          SessionSlotRepository sessionSlotRepository,
                          GroupClassRepository groupClassRepository,
                          TeacherRepository teacherRepository,
                          StudentRepository studentRepository) {
        this.moduleCourseRepository = moduleCourseRepository;
        this.sessionSlotRepository = sessionSlotRepository;
        this.groupClassRepository = groupClassRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("modules", moduleCourseRepository.findAll());
        model.addAttribute("sessions", sessionSlotRepository.findAll(Sort.by(Sort.Direction.ASC, "startTime")));
        model.addAttribute("groups", groupClassRepository.findAll());
        model.addAttribute("teachersCount", teacherRepository.count());
        model.addAttribute("studentsCount", studentRepository.count());
        model.addAttribute("modulesCount", moduleCourseRepository.count());
        model.addAttribute("groupsCount", groupClassRepository.count());
        return "dashboard";
    }
}

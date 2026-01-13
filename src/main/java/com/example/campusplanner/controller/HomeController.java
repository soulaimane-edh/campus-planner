package com.example.campusplanner.controller;

import com.example.campusplanner.repository.GroupClassRepository;
import com.example.campusplanner.repository.EvaluationRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.SessionSlotRepository;
import com.example.campusplanner.repository.StudentRepository;
import com.example.campusplanner.repository.TeacherRepository;
import com.example.campusplanner.model.Role;
import com.example.campusplanner.model.Student;
import com.example.campusplanner.model.Teacher;
import com.example.campusplanner.model.UserAccount;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;
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
    private final EvaluationRepository evaluationRepository;

    public HomeController(ModuleCourseRepository moduleCourseRepository,
                          SessionSlotRepository sessionSlotRepository,
                          GroupClassRepository groupClassRepository,
                          TeacherRepository teacherRepository,
                          StudentRepository studentRepository,
                          EvaluationRepository evaluationRepository) {
        this.moduleCourseRepository = moduleCourseRepository;
        this.sessionSlotRepository = sessionSlotRepository;
        this.groupClassRepository = groupClassRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (currentUser.getRole() == Role.TEACHER) {
            Teacher teacher = teacherRepository.findByEmail(currentUser.getEmail()).orElse(null);
            if (teacher == null) {
                model.addAttribute("profileMissing", true);
                model.addAttribute("modules", Collections.emptyList());
                model.addAttribute("sessions", Collections.emptyList());
                model.addAttribute("evaluations", Collections.emptyList());
            } else {
                model.addAttribute("profileMissing", false);
                model.addAttribute("teacher", teacher);
                model.addAttribute("modules", moduleCourseRepository.findByResponsibleTeacherId(teacher.getId()));
                model.addAttribute("sessions", sessionSlotRepository.findByTeacherIdOrderByStartTimeAsc(teacher.getId()));
                model.addAttribute("evaluations", evaluationRepository.findByModuleCourseResponsibleTeacherIdOrderByEvaluationDateDesc(teacher.getId()));
            }
            return "dashboard-teacher";
        }
        if (currentUser.getRole() == Role.STUDENT) {
            Student student = studentRepository.findByEmail(currentUser.getEmail()).orElse(null);
            if (student == null || student.getGroupClass() == null) {
                model.addAttribute("profileMissing", true);
                model.addAttribute("modules", Collections.emptySet());
                model.addAttribute("sessions", Collections.emptyList());
                model.addAttribute("evaluations", Collections.emptyList());
            } else {
                model.addAttribute("profileMissing", false);
                model.addAttribute("student", student);
                model.addAttribute("modules", student.getGroupClass().getModules());
                model.addAttribute("sessions", sessionSlotRepository.findByGroupClassIdOrderByStartTimeAsc(student.getGroupClass().getId()));
                model.addAttribute("evaluations", evaluationRepository.findByStudentIdOrderByEvaluationDateDesc(student.getId()));
            }
            return "dashboard-student";
        }
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

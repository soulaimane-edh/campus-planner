package com.example.campusplanner.controller;

import com.example.campusplanner.model.Evaluation;
import com.example.campusplanner.model.ModuleCourse;
import com.example.campusplanner.model.Role;
import com.example.campusplanner.model.Student;
import com.example.campusplanner.model.Teacher;
import com.example.campusplanner.model.UserAccount;
import com.example.campusplanner.repository.EvaluationRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.StudentRepository;
import com.example.campusplanner.repository.TeacherRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/evaluations")
public class EvaluationController {

    private final EvaluationRepository evaluationRepository;
    private final StudentRepository studentRepository;
    private final ModuleCourseRepository moduleCourseRepository;
    private final TeacherRepository teacherRepository;

    public EvaluationController(EvaluationRepository evaluationRepository,
                                StudentRepository studentRepository,
                                ModuleCourseRepository moduleCourseRepository,
                                TeacherRepository teacherRepository) {
        this.evaluationRepository = evaluationRepository;
        this.studentRepository = studentRepository;
        this.moduleCourseRepository = moduleCourseRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        if (currentUser != null && currentUser.getRole() == Role.STUDENT) {
            return "redirect:/evaluations/my";
        }
        fillLists(model, currentUser);
        model.addAttribute("evaluationRequest", new EvaluationRequest());
        return "evaluations/list";
    }

    @GetMapping("/my")
    public String myEvaluations(Model model, HttpSession session) {
        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (currentUser.getRole() != Role.STUDENT) {
            return "redirect:/evaluations";
        }
        Student student = studentRepository.findByEmail(currentUser.getEmail()).orElse(null);
        model.addAttribute("evaluations", student == null
                ? Collections.emptyList()
                : evaluationRepository.findByStudentIdOrderByEvaluationDateDesc(student.getId()));
        model.addAttribute("profileMissing", student == null);
        return "evaluations/my";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("evaluationRequest") EvaluationRequest evaluationRequest,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Merci de remplir tous les champs obligatoires");
            UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
            fillLists(model, currentUser);
            model.addAttribute("evaluationRequest", evaluationRequest);
            return "evaluations/list";
        }

        UserAccount currentUser = (UserAccount) session.getAttribute("currentUser");
        Student student = studentRepository.findById(evaluationRequest.getStudentId()).orElseThrow();
        ModuleCourse module = moduleCourseRepository.findById(evaluationRequest.getModuleId()).orElseThrow();
        if (currentUser != null && currentUser.getRole() == Role.TEACHER) {
            Teacher teacher = teacherRepository.findByEmail(currentUser.getEmail()).orElse(null);
            if (teacher == null || module.getResponsibleTeacher() == null || !module.getResponsibleTeacher().getId().equals(teacher.getId())) {
                model.addAttribute("error", "Vous ne pouvez saisir des évaluations que pour vos modules.");
                fillLists(model, currentUser);
                model.addAttribute("evaluationRequest", evaluationRequest);
                return "evaluations/list";
            }
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setStudent(student);
        evaluation.setModuleCourse(module);
        evaluation.setAssessmentType(evaluationRequest.getAssessmentType());
        evaluation.setGrade(evaluationRequest.getGrade());
        evaluation.setEvaluationDate(evaluationRequest.getEvaluationDate());
        evaluation.setRemarks(evaluationRequest.getRemarks());

        evaluationRepository.save(evaluation);
        return "redirect:/evaluations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        evaluationRepository.deleteById(id);
        return "redirect:/evaluations";
    }

    private void fillLists(Model model, UserAccount currentUser) {
        if (currentUser != null && currentUser.getRole() == Role.TEACHER) {
            Teacher teacher = teacherRepository.findByEmail(currentUser.getEmail()).orElse(null);
            List<ModuleCourse> modules = teacher == null
                    ? Collections.emptyList()
                    : moduleCourseRepository.findByResponsibleTeacherId(teacher.getId());
            model.addAttribute("evaluations", teacher == null
                    ? Collections.emptyList()
                    : evaluationRepository.findByModuleCourseResponsibleTeacherIdOrderByEvaluationDateDesc(teacher.getId()));
            model.addAttribute("modules", modules);
            model.addAttribute("students", studentRepository.findAll());
            model.addAttribute("profileMissing", teacher == null);
            return;
        }
        model.addAttribute("evaluations", evaluationRepository.findAll(Sort.by(Sort.Direction.DESC, "evaluationDate")));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("modules", moduleCourseRepository.findAll());
        model.addAttribute("profileMissing", false);
    }
}

package com.example.campusplanner.controller;

import com.example.campusplanner.model.Evaluation;
import com.example.campusplanner.model.ModuleCourse;
import com.example.campusplanner.model.Student;
import com.example.campusplanner.repository.EvaluationRepository;
import com.example.campusplanner.repository.ModuleCourseRepository;
import com.example.campusplanner.repository.StudentRepository;
import jakarta.validation.Valid;
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

    public EvaluationController(EvaluationRepository evaluationRepository,
                                StudentRepository studentRepository,
                                ModuleCourseRepository moduleCourseRepository) {
        this.evaluationRepository = evaluationRepository;
        this.studentRepository = studentRepository;
        this.moduleCourseRepository = moduleCourseRepository;
    }

    @GetMapping
    public String list(Model model) {
        fillLists(model);
        model.addAttribute("evaluationRequest", new EvaluationRequest());
        return "evaluations/list";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("evaluationRequest") EvaluationRequest evaluationRequest,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Merci de remplir tous les champs obligatoires");
            fillLists(model);
            model.addAttribute("evaluationRequest", evaluationRequest);
            return "evaluations/list";
        }

        Student student = studentRepository.findById(evaluationRequest.getStudentId()).orElseThrow();
        ModuleCourse module = moduleCourseRepository.findById(evaluationRequest.getModuleId()).orElseThrow();

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

    private void fillLists(Model model) {
        model.addAttribute("evaluations", evaluationRepository.findAll(Sort.by(Sort.Direction.DESC, "evaluationDate")));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("modules", moduleCourseRepository.findAll());
    }
}

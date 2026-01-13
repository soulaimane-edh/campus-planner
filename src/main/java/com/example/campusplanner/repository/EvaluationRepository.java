package com.example.campusplanner.repository;

import com.example.campusplanner.model.Evaluation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByStudentIdOrderByEvaluationDateDesc(Long studentId);
    List<Evaluation> findByModuleCourseResponsibleTeacherIdOrderByEvaluationDateDesc(Long teacherId);
}

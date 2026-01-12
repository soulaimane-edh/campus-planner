package com.example.campusplanner.repository;

import com.example.campusplanner.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}

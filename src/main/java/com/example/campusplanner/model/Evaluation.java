package com.example.campusplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private ModuleCourse moduleCourse;

    private String assessmentType; // Examen, TD, Projet...

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private Double grade;

    @NotNull
    private LocalDate evaluationDate;

    private String remarks;

    public Evaluation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ModuleCourse getModuleCourse() {
        return moduleCourse;
    }

    public void setModuleCourse(ModuleCourse moduleCourse) {
        this.moduleCourse = moduleCourse;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

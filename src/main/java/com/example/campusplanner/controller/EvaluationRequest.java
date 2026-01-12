package com.example.campusplanner.controller;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class EvaluationRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long moduleId;

    private String assessmentType;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private Double grade;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate evaluationDate;

    private String remarks;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

package com.example.campusplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    private String specialty;
    private String competencies;
    private Integer workloadHours;

    @OneToMany(mappedBy = "responsibleTeacher")
    private Set<ModuleCourse> modules = new HashSet<>();

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCompetencies() {
        return competencies;
    }

    public void setCompetencies(String competencies) {
        this.competencies = competencies;
    }

    public Integer getWorkloadHours() {
        return workloadHours;
    }

    public void setWorkloadHours(Integer workloadHours) {
        this.workloadHours = workloadHours;
    }

    public Set<ModuleCourse> getModules() {
        return modules;
    }

    public void setModules(Set<ModuleCourse> modules) {
        this.modules = modules;
    }
}

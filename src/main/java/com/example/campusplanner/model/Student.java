package com.example.campusplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Student {

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

    @NotBlank
    private String fieldOfStudy; // filiere

    @NotBlank
    private String level; // niveau

    @ManyToOne
    private GroupClass groupClass;

    private String competencies;

    public Student() {
    }

    public Student(String firstName, String lastName, String email, String fieldOfStudy, String level) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fieldOfStudy = fieldOfStudy;
        this.level = level;
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

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public GroupClass getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(GroupClass groupClass) {
        this.groupClass = groupClass;
    }

    public String getCompetencies() {
        return competencies;
    }

    public void setCompetencies(String competencies) {
        this.competencies = competencies;
    }
}

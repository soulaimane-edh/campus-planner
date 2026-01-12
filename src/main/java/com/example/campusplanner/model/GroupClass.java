package com.example.campusplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GroupClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name; // TD/TP group label

    @NotBlank
    private String level;

    @NotBlank
    private String fieldOfStudy;

    @ManyToMany(mappedBy = "groups")
    private Set<ModuleCourse> modules = new HashSet<>();

    public GroupClass() {
    }

    public GroupClass(String name, String level, String fieldOfStudy) {
        this.name = name;
        this.level = level;
        this.fieldOfStudy = fieldOfStudy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Set<ModuleCourse> getModules() {
        return modules;
    }

    public void setModules(Set<ModuleCourse> modules) {
        this.modules = modules;
    }
}

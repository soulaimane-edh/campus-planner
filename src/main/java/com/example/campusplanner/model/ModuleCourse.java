package com.example.campusplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ModuleCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    @NotBlank
    private String fieldOfStudy; // filiere

    private String semester;
    private Integer volumeHours;
    private String requiredCompetencies;

    @ManyToOne
    private Teacher responsibleTeacher;

    @ManyToMany
    @JoinTable(name = "module_group",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<GroupClass> groups = new HashSet<>();

    public ModuleCourse() {
    }

    public ModuleCourse(String code, String name, String fieldOfStudy, String semester) {
        this.code = code;
        this.name = name;
        this.fieldOfStudy = fieldOfStudy;
        this.semester = semester;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getVolumeHours() {
        return volumeHours;
    }

    public void setVolumeHours(Integer volumeHours) {
        this.volumeHours = volumeHours;
    }

    public String getRequiredCompetencies() {
        return requiredCompetencies;
    }

    public void setRequiredCompetencies(String requiredCompetencies) {
        this.requiredCompetencies = requiredCompetencies;
    }

    public Teacher getResponsibleTeacher() {
        return responsibleTeacher;
    }

    public void setResponsibleTeacher(Teacher responsibleTeacher) {
        this.responsibleTeacher = responsibleTeacher;
    }

    public Set<GroupClass> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupClass> groups) {
        this.groups = groups;
    }
}

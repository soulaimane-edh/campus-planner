package com.example.campusplanner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class SessionSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ModuleCourse moduleCourse;

    @ManyToOne(optional = false)
    private Teacher teacher;

    @ManyToOne(optional = false)
    private GroupClass groupClass;

    @NotBlank
    private String room;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    private String sessionType; // CM/TD/TP

    public SessionSlot() {
    }

    public SessionSlot(ModuleCourse moduleCourse, Teacher teacher, GroupClass groupClass, String room, LocalDateTime startTime, LocalDateTime endTime) {
        this.moduleCourse = moduleCourse;
        this.teacher = teacher;
        this.groupClass = groupClass;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModuleCourse getModuleCourse() {
        return moduleCourse;
    }

    public void setModuleCourse(ModuleCourse moduleCourse) {
        this.moduleCourse = moduleCourse;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public GroupClass getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(GroupClass groupClass) {
        this.groupClass = groupClass;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }
}

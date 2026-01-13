package com.example.campusplanner.repository;

import com.example.campusplanner.model.ModuleCourse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleCourseRepository extends JpaRepository<ModuleCourse, Long> {
    List<ModuleCourse> findByResponsibleTeacherId(Long teacherId);
}

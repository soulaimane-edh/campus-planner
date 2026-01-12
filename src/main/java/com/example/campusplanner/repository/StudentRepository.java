package com.example.campusplanner.repository;

import com.example.campusplanner.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

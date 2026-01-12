package com.example.campusplanner.config;

import com.example.campusplanner.model.*;
import com.example.campusplanner.repository.*;
import com.example.campusplanner.service.TimetableService;
import com.example.campusplanner.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ModuleCourseRepository moduleCourseRepository;
    private final GroupClassRepository groupClassRepository;
    private final TimetableService timetableService;
    private final EvaluationRepository evaluationRepository;

    public DataInitializer(UserService userService, TeacherRepository teacherRepository, StudentRepository studentRepository,
                           ModuleCourseRepository moduleCourseRepository, GroupClassRepository groupClassRepository,
                           TimetableService timetableService, EvaluationRepository evaluationRepository) {
        this.userService = userService;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.moduleCourseRepository = moduleCourseRepository;
        this.groupClassRepository = groupClassRepository;
        this.timetableService = timetableService;
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public void run(String... args) {
        // comptes
        userService.createUser("Admin", "admin@local", "admin123", Role.ADMIN);
        userService.createUser("Prof Karim", "karim@local", "teacher", Role.TEACHER);
        userService.createUser("Etudiante Sara", "sara@local", "student", Role.STUDENT);

        Teacher karim = new Teacher("Karim", "Akoudad", "karim@local");
        karim.setSpecialty("Informatique");
        karim.setCompetencies("Java, Spring, UML");
        karim.setWorkloadHours(192);
        teacherRepository.save(karim);

        Teacher nada = new Teacher("Nada", "Duran", "nada@local");
        nada.setSpecialty("Réseaux");
        nada.setCompetencies("Cisco, Sécurité");
        teacherRepository.save(nada);

        GroupClass g1 = new GroupClass("G1", "L3", "Informatique");
        GroupClass g2 = new GroupClass("G2", "L3", "Informatique");
        groupClassRepository.saveAll(List.of(g1, g2));

        ModuleCourse m1 = new ModuleCourse("M310", "Spring MVC", "Informatique", "S5");
        m1.setVolumeHours(36);
        m1.setResponsibleTeacher(karim);
        m1.setRequiredCompetencies("Java, HTML");

        ModuleCourse m2 = new ModuleCourse("M330", "Sécurité", "Informatique", "S5");
        m2.setVolumeHours(24);
        m2.setResponsibleTeacher(nada);
        m2.setRequiredCompetencies("Réseaux");

        m1.getGroups().add(g1);
        m2.getGroups().add(g1);
        m2.getGroups().add(g2);

        moduleCourseRepository.saveAll(List.of(m1, m2));

        Student sara = new Student("Sara", "Ben Ali", "sara@local", "Informatique", "L3");
        sara.setGroupClass(g1);
        sara.setCompetencies("Java, UML");

        Student adam = new Student("Adam", "Kamal", "adam@local", "Informatique", "L3");
        adam.setGroupClass(g2);
        adam.setCompetencies("Python, DevOps");

        studentRepository.saveAll(List.of(sara, adam));

        Evaluation eval1 = new Evaluation();
        eval1.setStudent(sara);
        eval1.setModuleCourse(m1);
        eval1.setAssessmentType("TD");
        eval1.setGrade(15.5);
        eval1.setEvaluationDate(LocalDate.now().minusDays(3));
        eval1.setRemarks("Travail régulier");

        Evaluation eval2 = new Evaluation();
        eval2.setStudent(adam);
        eval2.setModuleCourse(m2);
        eval2.setAssessmentType("Examen");
        eval2.setGrade(13.0);
        eval2.setEvaluationDate(LocalDate.now().minusDays(7));
        eval2.setRemarks("Bonne progression");

        evaluationRepository.saveAll(List.of(eval1, eval2));

        // timetable sample
        SessionSlot slot1 = new SessionSlot(m1, karim, g1, "Salle A1",
                LocalDateTime.now().withHour(9).withMinute(0),
                LocalDateTime.now().withHour(11).withMinute(0));
        slot1.setSessionType("TD");
        timetableService.schedule(slot1);

        SessionSlot slot2 = new SessionSlot(m2, nada, g2, "Salle B2",
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(16).withMinute(0));
        slot2.setSessionType("TP");
        timetableService.schedule(slot2);
    }
}

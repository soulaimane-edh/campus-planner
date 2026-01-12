package com.example.campusplanner.service;

import com.example.campusplanner.model.SessionSlot;
import com.example.campusplanner.repository.SessionSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

@Service
public class TimetableService {

    private final SessionSlotRepository sessionSlotRepository;

    public TimetableService(SessionSlotRepository sessionSlotRepository) {
        this.sessionSlotRepository = sessionSlotRepository;
    }

    @Transactional
    public SessionSlot schedule(SessionSlot slot) {
        Objects.requireNonNull(slot.getStartTime(), "Début obligatoire");
        Objects.requireNonNull(slot.getEndTime(), "Fin obligatoire");
        if (!slot.getEndTime().isAfter(slot.getStartTime())) {
            throw new IllegalArgumentException("L'heure de fin doit être après le début");
        }
        List<String> conflicts = detectConflicts(slot);
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", conflicts));
        }
        return sessionSlotRepository.save(slot);
    }

    public List<String> detectConflicts(SessionSlot slot) {
        List<String> conflicts = new ArrayList<>();
        LocalDateTime start = slot.getStartTime();
        LocalDateTime end = slot.getEndTime();

        if (!sessionSlotRepository.findByRoomAndStartTimeLessThanAndEndTimeGreaterThan(slot.getRoom(), end, start).isEmpty()) {
            conflicts.add("Salle " + slot.getRoom() + " déjà occupée");
        }
        if (!sessionSlotRepository.findByTeacherIdAndStartTimeLessThanAndEndTimeGreaterThan(slot.getTeacher().getId(), end, start).isEmpty()) {
            conflicts.add("Enseignant déjà occupé");
        }
        if (!sessionSlotRepository.findByGroupClassIdAndStartTimeLessThanAndEndTimeGreaterThan(slot.getGroupClass().getId(), end, start).isEmpty()) {
            conflicts.add("Groupe déjà occupé");
        }
        if (slot.getModuleCourse().getResponsibleTeacher() != null &&
                !slot.getModuleCourse().getResponsibleTeacher().getId().equals(slot.getTeacher().getId())) {
            conflicts.add("Enseignant non affecté à ce module");
        }
        if (!slot.getModuleCourse().getGroups().isEmpty() &&
                !slot.getModuleCourse().getGroups().contains(slot.getGroupClass())) {
            conflicts.add("Groupe non affecté à ce module");
        }
        return conflicts;
    }

    public List<SessionSlot> list() {
        return sessionSlotRepository.findAll();
    }
}

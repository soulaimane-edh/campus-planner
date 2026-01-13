package com.example.campusplanner.repository;

import com.example.campusplanner.model.SessionSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionSlotRepository extends JpaRepository<SessionSlot, Long> {
    List<SessionSlot> findByRoomAndStartTimeLessThanAndEndTimeGreaterThan(String room, LocalDateTime end, LocalDateTime start);
    List<SessionSlot> findByTeacherIdAndStartTimeLessThanAndEndTimeGreaterThan(Long teacherId, LocalDateTime end, LocalDateTime start);
    List<SessionSlot> findByGroupClassIdAndStartTimeLessThanAndEndTimeGreaterThan(Long groupId, LocalDateTime end, LocalDateTime start);
    List<SessionSlot> findByTeacherIdOrderByStartTimeAsc(Long teacherId);
    List<SessionSlot> findByGroupClassIdOrderByStartTimeAsc(Long groupId);
}

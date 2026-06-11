package br.com.tauan.agendamento.timeslot.infrastructure.persistence.repository;

import br.com.tauan.agendamento.timeslot.infrastructure.persistence.entity.TimeSlotJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface SpringDataTimeSlotRepository
        extends JpaRepository<TimeSlotJpaEntity, UUID> {

    List<TimeSlotJpaEntity> findByCalendarId(UUID calendarId);

    @Query("""
        SELECT COUNT(ts) > 0
        FROM TimeSlotJpaEntity ts
        WHERE ts.calendarId = :calendarId
          AND :startTime < ts.endTime
          AND :endTime > ts.startTime
    """)
    boolean existsOverlappingTimeSlot(
            @Param("calendarId") UUID calendarId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
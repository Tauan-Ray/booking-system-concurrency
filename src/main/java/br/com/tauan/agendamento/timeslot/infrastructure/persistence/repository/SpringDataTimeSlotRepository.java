package br.com.tauan.agendamento.timeslot.infrastructure.persistence.repository;

import br.com.tauan.agendamento.timeslot.infrastructure.persistence.entity.TimeSlotJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataTimeSlotRepository
        extends JpaRepository<TimeSlotJpaEntity, UUID> {

    Optional<TimeSlotJpaEntity> findByIdAndDeletedAtIsNull(UUID id);
    List<TimeSlotJpaEntity> findByCalendarIdAndDeletedAtIsNull(UUID calendarId);
    List<TimeSlotJpaEntity> findAllByDeletedAtIsNull();

    @Query("""
        SELECT COUNT(ts) > 0
        FROM TimeSlotJpaEntity ts
        WHERE ts.calendarId = :calendarId
          AND ts.deletedAt IS NULL
          AND :startTime < ts.endTime
          AND :endTime > ts.startTime
    """)
    boolean existsOverlappingTimeSlot(
            UUID calendarId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}
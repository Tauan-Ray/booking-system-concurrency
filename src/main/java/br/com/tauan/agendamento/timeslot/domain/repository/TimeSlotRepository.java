package br.com.tauan.agendamento.timeslot.domain.repository;

import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimeSlotRepository {
    List<TimeSlot> findAll();
    Optional<TimeSlot> findById(UUID id);
    Optional<TimeSlot> findByIdForUpdate(UUID id);
    List<TimeSlot> findByCalendarId(UUID calendarId);
    boolean existsOverlappingTimeSlot(
            UUID calendarId,
            LocalTime startTime,
            LocalTime endTime
    );
    TimeSlot save(TimeSlot timeSlot);
}

package br.com.tauan.agendamento.timeslot.domain.entity;

import br.com.tauan.agendamento.shared.domain.entity.BaseEntity;
import br.com.tauan.agendamento.timeslot.domain.exception.InvalidTimeSlotException;
import br.com.tauan.agendamento.timeslot.domain.exception.TimeSlotAlreadyDeletedException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class TimeSlot extends BaseEntity {

    private UUID calendarId;
    private LocalTime startTime;
    private LocalTime endTime;

    private TimeSlot(
            UUID id,
            UUID calendarId,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        super(
                id,
                createdAt,
                updatedAt,
                deletedAt
        );

        this.calendarId = calendarId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeSlot create(
            UUID calendarId, LocalTime startTime, LocalTime endTime
    ) {
        LocalDateTime now = LocalDateTime.now();

        validate(calendarId, startTime, endTime);

        return new TimeSlot(
                UUID.randomUUID(),
                calendarId,
                startTime,
                endTime,
                now,
                now,
                null
        );
    }

    public static TimeSlot restore(
            UUID id,
            UUID calendarId,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        validate(calendarId, startTime, endTime);

        return new TimeSlot(
                id,
                calendarId,
                startTime,
                endTime,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    private static void validate(
            UUID calendarId, LocalTime startTime, LocalTime endTime
    ) {
        if (calendarId == null) {
            throw new InvalidTimeSlotException("Calendar id cannot be empty");
        }

        if (startTime == null) {
            throw new InvalidTimeSlotException("Start time cannot be null");
        }

        if (endTime == null) {
            throw new InvalidTimeSlotException("End time cannot be null");
        }

        if (!startTime.isBefore(endTime)) {
            throw new InvalidTimeSlotException(
                    "Start time must be before end time"
            );
        }
    }

    public void archive() {
        if (isDeleted()) {
            throw new TimeSlotAlreadyDeletedException();
        }

        this.deletedAt = LocalDateTime.now();
        touch();
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

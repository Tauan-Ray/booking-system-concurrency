package br.com.tauan.agendamento.timeslot.application.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record TimeSlotOutput(
        UUID id,
        UUID calendarId,
        LocalTime startTime,
        LocalTime endTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt

) {}

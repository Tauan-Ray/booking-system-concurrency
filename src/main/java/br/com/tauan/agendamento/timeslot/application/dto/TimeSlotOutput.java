package br.com.tauan.agendamento.timeslot.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TimeSlotOutput(
        UUID id,
        UUID calendarId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt

) {}

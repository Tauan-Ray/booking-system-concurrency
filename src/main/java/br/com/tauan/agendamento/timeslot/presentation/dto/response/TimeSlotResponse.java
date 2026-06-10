package br.com.tauan.agendamento.timeslot.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TimeSlotResponse(
        UUID id,
        UUID calendarId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {}

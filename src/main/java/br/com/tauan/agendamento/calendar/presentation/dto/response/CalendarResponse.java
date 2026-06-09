package br.com.tauan.agendamento.calendar.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CalendarResponse(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {}

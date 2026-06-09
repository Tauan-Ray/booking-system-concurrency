package br.com.tauan.agendamento.calendar.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CalendarOutput(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {}

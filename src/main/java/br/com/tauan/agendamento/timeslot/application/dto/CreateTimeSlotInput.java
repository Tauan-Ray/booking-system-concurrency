package br.com.tauan.agendamento.timeslot.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTimeSlotInput(
        UUID calendarId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}

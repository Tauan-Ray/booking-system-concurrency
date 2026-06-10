package br.com.tauan.agendamento.timeslot.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateTimeSlotRequest(
        @NotNull
        UUID calendarId,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime
) {}

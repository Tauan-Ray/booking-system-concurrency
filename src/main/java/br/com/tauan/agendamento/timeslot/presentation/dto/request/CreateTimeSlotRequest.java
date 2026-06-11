package br.com.tauan.agendamento.timeslot.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

public record CreateTimeSlotRequest(
        @NotNull
        UUID calendarId,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime
) {}

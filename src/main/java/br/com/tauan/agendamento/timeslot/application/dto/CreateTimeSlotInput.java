package br.com.tauan.agendamento.timeslot.application.dto;

import java.time.LocalTime;
import java.util.UUID;

public record CreateTimeSlotInput(
        UUID calendarId,
        LocalTime startTime,
        LocalTime endTime
) {}

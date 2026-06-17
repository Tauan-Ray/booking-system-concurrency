package br.com.tauan.agendamento.timeslot.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "Dados necessários para criar uma faixa de horário (time slot) em uma agenda.")
public record CreateTimeSlotRequest(
        @Schema(description = "Identificador da agenda à qual o horário pertence.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID calendarId,

        @Schema(description = "Horário de início, no formato HH:mm:ss.", example = "08:00:00", type = "string")
        @NotNull
        LocalTime startTime,

        @Schema(description = "Horário de término, no formato HH:mm:ss.", example = "09:00:00", type = "string")
        @NotNull
        LocalTime endTime
) {}

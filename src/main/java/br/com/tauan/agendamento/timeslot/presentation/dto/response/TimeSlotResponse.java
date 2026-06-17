package br.com.tauan.agendamento.timeslot.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Schema(description = "Representação de uma faixa de horário (time slot).")
public record TimeSlotResponse(
        @Schema(description = "Identificador único do horário.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Identificador da agenda à qual o horário pertence.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID calendarId,

        @Schema(description = "Horário de início, no formato HH:mm:ss.", example = "08:00:00", type = "string")
        LocalTime startTime,

        @Schema(description = "Horário de término, no formato HH:mm:ss.", example = "09:00:00", type = "string")
        LocalTime endTime,

        @Schema(description = "Data/hora de criação do registro.")
        LocalDateTime createdAt,

        @Schema(description = "Data/hora da última atualização.")
        LocalDateTime updatedAt,

        @Schema(description = "Data/hora do arquivamento (soft delete). Nulo se ativo.")
        LocalDateTime deletedAt
) {}

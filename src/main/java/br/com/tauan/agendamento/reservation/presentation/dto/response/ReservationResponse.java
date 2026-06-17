package br.com.tauan.agendamento.reservation.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Representação de uma reserva.")
public record ReservationResponse(
        @Schema(description = "Identificador único da reserva.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Identificador do usuário dono da reserva.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID userId,

        @Schema(description = "Identificador da faixa de horário reservada.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID timeSlotId,

        @Schema(description = "Status atual da reserva.", example = "CONFIRMED", allowableValues = {"CONFIRMED", "CANCELLED"})
        String status,

        @Schema(description = "Data da reserva, no formato AAAA-MM-DD.", example = "2026-06-20", type = "string")
        LocalDate reservationDate,

        @Schema(description = "Data/hora de criação do registro.")
        LocalDateTime createdAt,

        @Schema(description = "Data/hora da última atualização.")
        LocalDateTime updatedAt
) {}

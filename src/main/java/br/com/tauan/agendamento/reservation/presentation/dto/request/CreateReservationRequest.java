package br.com.tauan.agendamento.reservation.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados necessários para criar uma reserva de horário.")
public record CreateReservationRequest(
        @Schema(
                description = "Identificador do usuário que está reservando. Opcional: quando omitido, "
                        + "assume-se o usuário autenticado.",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                nullable = true
        )
        UUID userId,

        @Schema(description = "Identificador da faixa de horário a ser reservada.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull
        UUID timeSlotId,

        @Schema(description = "Data da reserva, no formato AAAA-MM-DD.", example = "2026-06-20", type = "string")
        @NotNull
        LocalDate reservationDate
) {}

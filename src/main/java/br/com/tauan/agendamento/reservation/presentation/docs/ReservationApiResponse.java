package br.com.tauan.agendamento.reservation.presentation.docs;

import br.com.tauan.agendamento.reservation.presentation.dto.response.ReservationResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ReservationApiResponse", description = "Envelope de sucesso contendo uma reserva.")
public record ReservationApiResponse(
        @Schema(example = "true")
        boolean success,

        ReservationResponse data
) {}

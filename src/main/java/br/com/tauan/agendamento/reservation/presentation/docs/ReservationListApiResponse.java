package br.com.tauan.agendamento.reservation.presentation.docs;

import br.com.tauan.agendamento.reservation.presentation.dto.response.ReservationResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ReservationListApiResponse", description = "Envelope de sucesso contendo uma lista de reservas.")
public record ReservationListApiResponse(
        @Schema(example = "true")
        boolean success,

        List<ReservationResponse> data
) {}

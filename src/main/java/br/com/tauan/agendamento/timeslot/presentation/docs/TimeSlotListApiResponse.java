package br.com.tauan.agendamento.timeslot.presentation.docs;

import br.com.tauan.agendamento.timeslot.presentation.dto.response.TimeSlotResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "TimeSlotListApiResponse", description = "Envelope de sucesso contendo uma lista de faixas de horário.")
public record TimeSlotListApiResponse(
        @Schema(example = "true")
        boolean success,

        List<TimeSlotResponse> data
) {}

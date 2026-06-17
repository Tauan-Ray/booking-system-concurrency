package br.com.tauan.agendamento.timeslot.presentation.docs;

import br.com.tauan.agendamento.timeslot.presentation.dto.response.TimeSlotResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TimeSlotApiResponse", description = "Envelope de sucesso contendo uma faixa de horário.")
public record TimeSlotApiResponse(
        @Schema(example = "true")
        boolean success,

        TimeSlotResponse data
) {}

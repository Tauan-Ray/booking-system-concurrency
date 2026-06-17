package br.com.tauan.agendamento.calendar.presentation.docs;

import br.com.tauan.agendamento.calendar.presentation.dto.response.CalendarResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CalendarApiResponse", description = "Envelope de sucesso contendo uma agenda.")
public record CalendarApiResponse(
        @Schema(example = "true")
        boolean success,

        CalendarResponse data
) {}

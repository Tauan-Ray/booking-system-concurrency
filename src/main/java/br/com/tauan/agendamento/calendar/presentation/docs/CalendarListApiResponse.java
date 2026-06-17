package br.com.tauan.agendamento.calendar.presentation.docs;

import br.com.tauan.agendamento.calendar.presentation.dto.response.CalendarResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "CalendarListApiResponse", description = "Envelope de sucesso contendo uma lista de agendas.")
public record CalendarListApiResponse(
        @Schema(example = "true")
        boolean success,

        List<CalendarResponse> data
) {}

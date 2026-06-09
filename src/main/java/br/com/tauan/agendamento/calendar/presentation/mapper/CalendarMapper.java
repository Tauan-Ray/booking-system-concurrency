package br.com.tauan.agendamento.calendar.presentation.mapper;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.dto.CreateCalendarInput;
import br.com.tauan.agendamento.calendar.presentation.dto.request.CreateCalendarRequest;
import br.com.tauan.agendamento.calendar.presentation.dto.response.CalendarResponse;

public class CalendarMapper {

    public static CalendarResponse toResponse(CalendarOutput output) {
        return new CalendarResponse(
                output.id(),
                output.name(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static CreateCalendarInput toInput(CreateCalendarRequest request) {
        return new CreateCalendarInput(
                request.name()
        );
    }
}

package br.com.tauan.agendamento.calendar.application.mapper;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;

public class CalendarMapper {
    public static CalendarOutput toOutput(Calendar calendar) {
        return new CalendarOutput(
                calendar.getId(),
                calendar.getName(),
                calendar.getCreatedAt(),
                calendar.getUpdatedAt(),
                calendar.getDeletedAt()
        );
    }
}

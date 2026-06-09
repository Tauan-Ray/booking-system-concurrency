package br.com.tauan.agendamento.calendar.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class CalendarAlreadyExistsException extends DomainException {
    public CalendarAlreadyExistsException() {
        super("Calendar already exists");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "CALENDAR_ALREADY_EXISTS";
    }
}

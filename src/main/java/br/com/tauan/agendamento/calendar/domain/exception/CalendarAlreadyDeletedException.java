package br.com.tauan.agendamento.calendar.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class CalendarAlreadyDeletedException extends DomainException {

    public CalendarAlreadyDeletedException() {
        super("Calendar already deleted");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "CALENDAR_ALREADY_DELETED";
    }
}



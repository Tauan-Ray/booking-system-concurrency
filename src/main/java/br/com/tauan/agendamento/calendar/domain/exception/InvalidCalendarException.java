package br.com.tauan.agendamento.calendar.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class InvalidCalendarException extends DomainException {

    public InvalidCalendarException(String message) {
        super(message);
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public String getCode() {
        return "INVALID_CALENDAR";
    }
}
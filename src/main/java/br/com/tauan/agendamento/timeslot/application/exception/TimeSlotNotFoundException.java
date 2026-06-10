package br.com.tauan.agendamento.timeslot.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class TimeSlotNotFoundException extends DomainException {
    public TimeSlotNotFoundException() {
        super("Time slot not found");
    }

    @Override
    public int getStatus() {
        return 404;
    }

    @Override
    public String getCode() {
        return "TIME_SLOT_NOT_FOUND";
    }
}

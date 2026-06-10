package br.com.tauan.agendamento.timeslot.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class TimeSlotAlreadyDeletedException extends DomainException {

    public TimeSlotAlreadyDeletedException() {
        super("Time slot already deleted");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "TIME_SLOT_ALREADY_DELETED";
    }
}



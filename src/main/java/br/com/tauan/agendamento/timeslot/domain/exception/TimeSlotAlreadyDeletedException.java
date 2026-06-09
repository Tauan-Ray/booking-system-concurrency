package br.com.tauan.agendamento.timeslot.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class TimeSlotAlreadyDeletedException extends DomainException {

    public TimeSlotAlreadyDeletedException() {
        super("TimeSlot already deleted");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "TIMESLOT_ALREADY_DELETED";
    }
}



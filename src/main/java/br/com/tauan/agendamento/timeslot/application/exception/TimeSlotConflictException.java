package br.com.tauan.agendamento.timeslot.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class TimeSlotConflictException extends DomainException {
    public TimeSlotConflictException() {
        super("Time slot overlaps with an existing time slot");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "TIME_SLOT_CONFLICT";
    }
}

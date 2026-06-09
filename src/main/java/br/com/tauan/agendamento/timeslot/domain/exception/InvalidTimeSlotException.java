package br.com.tauan.agendamento.timeslot.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class InvalidTimeSlotException extends DomainException {

    public InvalidTimeSlotException(String message) {
        super(message);
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public String getCode() {
        return "INVALID_TIMESLOT";
    }
}

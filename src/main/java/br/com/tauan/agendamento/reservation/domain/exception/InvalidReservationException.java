package br.com.tauan.agendamento.reservation.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class InvalidReservationException extends DomainException {

    public InvalidReservationException(String message) {
        super(message);
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public String getCode() {
        return "INVALID_RESERVATION";
    }
}
package br.com.tauan.agendamento.reservation.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class ReservationNotFoundFoundException extends DomainException {
    public ReservationNotFoundFoundException() {
        super("Reservation not found");
    }

    @Override
    public int getStatus() {
        return 404;
    }

    @Override
    public String getCode() {
        return "RESERVATION_NOT_FOUND";
    }
}

package br.com.tauan.agendamento.reservation.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException() {
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

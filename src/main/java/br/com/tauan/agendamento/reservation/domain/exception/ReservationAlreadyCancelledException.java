package br.com.tauan.agendamento.reservation.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class ReservationAlreadyCancelledException extends DomainException {

    public ReservationAlreadyCancelledException() {
        super("Reservation already cancelled");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "RESERVATION_ALREADY_CANCELLED";
    }
}
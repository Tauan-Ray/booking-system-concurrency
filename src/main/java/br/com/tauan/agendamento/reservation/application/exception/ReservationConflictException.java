package br.com.tauan.agendamento.reservation.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class ReservationConflictException extends DomainException {

  public ReservationConflictException() {
    super("Time slot is already reserved for this date");
  }

  @Override
  public int getStatus() {
    return 409;
  }

  @Override
  public String getCode() {
    return "RESERVATION_CONFLICT";
  }
}
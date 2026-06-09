package br.com.tauan.agendamento.calendar.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class CalendarNotFoundException extends DomainException {
    public CalendarNotFoundException() {
        super("Calendar not exists");
    }

  @Override
  public int getStatus() {
    return 404;
  }

  @Override
  public String getCode() {
    return "CALENDAR_NOT_EXISTS";
  }
}

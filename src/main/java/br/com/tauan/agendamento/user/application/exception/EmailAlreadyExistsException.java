package br.com.tauan.agendamento.user.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException() {
        super("Email already exists");
    }

    @Override
    public int getStatus() {
        return 429;
    }

    @Override
    public String getCode() {
        return "EMAIL_ALREADY_EXISTS";
    }
}

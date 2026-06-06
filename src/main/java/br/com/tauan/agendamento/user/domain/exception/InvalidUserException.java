package br.com.tauan.agendamento.user.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class InvalidUserException extends DomainException {

    public InvalidUserException(String message) {
        super(message);
    }

    @Override
    public int getStatus() {
        return 400;
    }

    @Override
    public String getCode() {
        return "INVALID_USER";
    }
}
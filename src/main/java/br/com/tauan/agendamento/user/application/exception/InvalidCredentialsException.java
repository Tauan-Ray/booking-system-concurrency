package br.com.tauan.agendamento.user.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }

    @Override
    public int getStatus() {
        return 401;
    }

    @Override
    public String getCode() {
        return "UNAUTHORIZED";
    }
}

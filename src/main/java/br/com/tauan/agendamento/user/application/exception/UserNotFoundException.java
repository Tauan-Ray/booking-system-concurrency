package br.com.tauan.agendamento.user.application.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException() {
        super("User not found");
    }

    @Override
    public int getStatus() {
        return 404;
    }

    @Override
    public String getCode() {
        return "USER_NOT_FOUND";
    }
}

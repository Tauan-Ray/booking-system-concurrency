package br.com.tauan.agendamento.user.domain.exception;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;

public class UserAlreadyDeletedException extends DomainException {

    public UserAlreadyDeletedException() {
        super("User already deleted");
    }

    @Override
    public int getStatus() {
        return 409;
    }

    @Override
    public String getCode() {
        return "USER_ALREADY_DELETED";
    }
}
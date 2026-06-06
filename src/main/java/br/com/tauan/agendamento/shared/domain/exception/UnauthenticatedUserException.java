package br.com.tauan.agendamento.shared.domain.exception;

public class UnauthenticatedUserException extends DomainException {
    public UnauthenticatedUserException() {
        super("User is not authenticated");
    }

    @Override
    public int getStatus() {
        return 401;
    }

    @Override
    public String getCode() {
        return "UNAUTHENTICATED_USER";
    }
}

package br.com.tauan.agendamento.shared.domain.exception;

public class ForbiddenException extends DomainException {

    public ForbiddenException() {
        super("You do not have permission to perform this action");
    }

    @Override
    public int getStatus() {
        return 403;
    }

    @Override
    public String getCode() {
        return "FORBIDDEN";
    }
}
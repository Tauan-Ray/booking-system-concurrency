package br.com.tauan.agendamento.shared.domain.exception;

public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    public abstract int getStatus();
    public abstract String getCode();
}

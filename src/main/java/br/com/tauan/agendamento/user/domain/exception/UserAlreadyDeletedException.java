package br.com.tauan.agendamento.user.domain.exception;

public class UserAlreadyDeletedException extends RuntimeException {
    public UserAlreadyDeletedException() {
        super("User already deleted");
    }
}

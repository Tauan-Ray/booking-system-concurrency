package br.com.tauan.agendamento.user.domain.valueobject;

import br.com.tauan.agendamento.user.domain.exception.InvalidUserException;

import java.util.regex.Pattern;

public class Email {
    private final String value;

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );


    public Email(String value) {
        validate(value);
        this.value = value.toLowerCase();
    }

    private void validate(String email) {
        if (email == null || email.isBlank()) {
            throw  new InvalidUserException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidUserException("Invalid email");
        }
    }

    public String getValue() {
        return value;
    }
}
package br.com.tauan.agendamento.user.domain.valueobject;

import br.com.tauan.agendamento.user.domain.exception.InvalidUserException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        Email email = new Email("tauan@email.com");

        assertEquals(
                "tauan@email.com",
                email.getValue()
        );
    }

    @Test
    void shouldConvertEmailToLowerCase() {
        Email email = new Email("Tauan@Email.Com");

        assertEquals(
                "tauan@email.com",
                email.getValue()
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        InvalidUserException exception =
            assertThrows(
                    InvalidUserException.class,
                    () -> new Email(null)
            );

        assertEquals(
                "Email cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        InvalidUserException exception =
                assertThrows(
                        InvalidUserException.class,
                        () -> new Email("")
                );

        assertEquals(
                "Email cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        InvalidUserException exception =
                assertThrows(
                        InvalidUserException.class,
                        () -> new Email("abc")
                );

        assertEquals(
                "Invalid email",
                exception.getMessage()
        );
    }
}

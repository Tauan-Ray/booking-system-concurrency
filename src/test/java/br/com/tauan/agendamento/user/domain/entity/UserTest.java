package br.com.tauan.agendamento.user.domain.entity;

import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.user.domain.enums.UserRole;
import br.com.tauan.agendamento.user.domain.exception.InvalidUserException;
import br.com.tauan.agendamento.user.domain.exception.UserAlreadyDeletedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserSuccessfully() {
        User user = UserTestBuilder.builder().build();

        assertEquals("Tauan", user.getName());

        assertEquals(
                "tauan@email.com",
                user.getEmail().getValue()
        );

        assertEquals(
                UserRole.USER,
                user.getRole()
        );

        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertNull(user.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidUserException exception =
            assertThrows(
                    InvalidUserException.class,
                    () -> UserTestBuilder.builder()
                            .withName(null)
                            .build()
            );

        assertEquals(
                "Name cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidUserException exception =
                assertThrows(
                        InvalidUserException.class,
                        () -> UserTestBuilder.builder()
                                .withName("")
                                .build()
                );

        assertEquals(
                "Name cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        InvalidUserException exception =
            assertThrows(
                    InvalidUserException.class,
                    () -> UserTestBuilder.builder()
                            .withPassword(null)
                            .build()
            );

        assertEquals(
                "Password cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsBlank() {
        InvalidUserException exception =
                assertThrows(
                        InvalidUserException.class,
                        () -> UserTestBuilder.builder()
                                .withPassword("")
                                .build()
                );

        assertEquals(
                "Password cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldDeactivateUser() {
        User user = UserTestBuilder.builder().build();

        user.deactivate();

        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenUserIsAlreadyDeleted() {
        User user = UserTestBuilder.builder().build();

        user.deactivate();

        UserAlreadyDeletedException exception =
            assertThrows(
                    UserAlreadyDeletedException.class,
                    user::deactivate
            );

        assertEquals(
                "User already deleted",
                exception.getMessage()
        );
    }
}

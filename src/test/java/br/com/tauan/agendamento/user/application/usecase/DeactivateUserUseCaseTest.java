package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.user.application.exception.UserNotFoundException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeactivateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private DeactivateUserUseCase useCase;

    @Test
    void shouldDeactivateOwnAccount() {
        User user = UserTestBuilder.builder().build();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(authenticatedUserProvider.getUserId())
                .thenReturn(user.getId());

        when(authenticatedUserProvider.hasRole("ADMIN"))
                .thenReturn(false);

        useCase.execute(user.getId());

        assertTrue(user.isDeleted());

        verify(userRepository)
                .findById(user.getId());
        verify(userRepository)
                .save(user);

        verify(authenticatedUserProvider)
                .getUserId();
        verify(authenticatedUserProvider)
                .hasRole("ADMIN");
    }

    @Test
    void shouldAllowAdminToDeactivateAnotherUser() {
        User user = UserTestBuilder.builder().build();
        UUID adminId = UUID.randomUUID();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(authenticatedUserProvider.getUserId())
                .thenReturn(adminId);

        when(authenticatedUserProvider.hasRole("ADMIN"))
                .thenReturn(true);

        useCase.execute(user.getId());

        assertTrue(user.isDeleted());

        verify(userRepository)
                .findById(user.getId());
        verify(userRepository)
                .save(user);

        verify(authenticatedUserProvider)
                .getUserId();
        verify(authenticatedUserProvider)
                .hasRole("ADMIN");
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserTriesToDeactivateAnotherUser() {
        User user = UserTestBuilder.builder().build();
        UUID otherUser = UUID.randomUUID();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(authenticatedUserProvider.getUserId())
                .thenReturn(otherUser);

        when(authenticatedUserProvider.hasRole("ADMIN"))
                .thenReturn(false);


        ForbiddenException exception =
            assertThrows(
                    ForbiddenException.class,
                    () -> useCase.execute(user.getId())
            );

        assertFalse(user.isDeleted());
        assertEquals(
                "You do not have permission to perform this action",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(user.getId());
        verify(userRepository, never())
                .save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        UserNotFoundException exception =
                assertThrows(
                        UserNotFoundException.class,
                        () -> useCase.execute(userId)
                );

        verifyNoInteractions(authenticatedUserProvider);

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(userId);
        verify(userRepository, never())
                .save(any());

    }
}
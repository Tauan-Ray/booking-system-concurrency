package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.user.application.dto.UserOutput;
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
class GetUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUserProvider auth;

    @InjectMocks
    private GetUserByIdUseCase useCase;

    @Test
    void shouldReturnUserWhenRequesterIsOwner() {
        User user = UserTestBuilder.builder().build();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(auth.getUserId())
                .thenReturn(user.getId());

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        UserOutput result = useCase.execute(user.getId());

        assertNotNull(result);

        assertEquals(
                user.getId(),
                result.id()
        );

        verify(userRepository)
                .findById(user.getId());

        verify(auth)
                .getUserId();

        verify(auth)
                .hasRole("ADMIN");
    }

    @Test
    void shouldReturnUserWhenRequesterIsAdmin() {
        User user = UserTestBuilder.builder().build();

        UUID adminId = UUID.randomUUID();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(auth.getUserId())
                .thenReturn(adminId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(true);

        UserOutput result = useCase.execute(user.getId());

        assertEquals(
                user.getId(),
                result.id()
        );

        verify(userRepository)
                .findById(user.getId());
    }

    @Test
    void shouldThrowForbiddenExceptionWhenUserTriesToAccessAnotherUser() {
        User user = UserTestBuilder.builder().build();

        UUID otherUserId = UUID.randomUUID();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(auth.getUserId())
                .thenReturn(otherUserId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        ForbiddenException exception =
                assertThrows(
                        ForbiddenException.class,
                        () -> useCase.execute(user.getId())
                );

        assertEquals(
                "You do not have permission to perform this action",
                exception.getMessage()
        );
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

        verifyNoInteractions(auth);

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(userId);
    }
}
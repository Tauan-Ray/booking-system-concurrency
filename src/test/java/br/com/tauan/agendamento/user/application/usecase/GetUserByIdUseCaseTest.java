package br.com.tauan.agendamento.user.application.usecase;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdUseCase useCase;

    @Test
    void shouldReturnUserWhenUserExists() {
        User user = UserTestBuilder.builder().build();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        User result = useCase.execute(user.getId());

        assertNotNull(result);

        assertEquals(
                user.getId(),
                result.getId()
        );

        verify(userRepository)
                .findById(user.getId());
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

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(userId);
    }
}

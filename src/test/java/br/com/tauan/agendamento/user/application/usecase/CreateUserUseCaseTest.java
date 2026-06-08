package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.PasswordEncoder;
import br.com.tauan.agendamento.test.factory.UserInputTestBuilder;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.application.exception.EmailAlreadyExistsException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.exception.InvalidUserException;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserInput input = UserInputTestBuilder.builder().build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("hashed-password");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = useCase.execute(input);

        assertEquals("Tauan", user.getName());
        assertEquals("tauan@email.com", user.getEmail().getValue());
        assertEquals(
                "hashed-password",
                user.getPassword()
        );

        verify(passwordEncoder)
                .encode("123456");

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        User existingUser =
                UserTestBuilder.builder().build();

        CreateUserInput input = UserInputTestBuilder.builder().build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(existingUser));

        EmailAlreadyExistsException exception =
                assertThrows(
                        EmailAlreadyExistsException.class,
                        () -> useCase.execute(input)
                );

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any());

        assertEquals(
                "Email already exists",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        CreateUserInput input = UserInputTestBuilder.builder()
                .withEmail("invalid")
                .build();

        InvalidUserException exception =
            assertThrows(
                    InvalidUserException.class,
                    () -> useCase.execute(input)
            );

        verifyNoInteractions(passwordEncoder);

        verify(userRepository, never())
                .save(any());

        assertEquals(
                "Invalid email",
                exception.getMessage()
        );
    }
}

package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.JwtProvider;
import br.com.tauan.agendamento.shared.application.contract.PasswordEncoder;
import br.com.tauan.agendamento.test.factory.LoginInputTestBuilder;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.user.application.dto.AuthOutput;
import br.com.tauan.agendamento.user.application.dto.LoginInput;
import br.com.tauan.agendamento.user.application.exception.InvalidCredentialsException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.exception.InvalidUserException;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private LoginUseCase useCase;

    @Test
    void shouldLoginSuccessfully() {
        User user = UserTestBuilder.builder().build();
        LoginInput input = LoginInputTestBuilder.builder().build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                input.password(),
                user.getPassword()
        )).thenReturn(true);

        when(jwtProvider.generateToken(
                user.getId().toString(),
                user.getEmail().getValue(),
                user.getRole().name()
        )).thenReturn("jwt-token");

        AuthOutput output = useCase.execute(input);

        assertNotNull(output);
        assertEquals("jwt-token", output.token());

        verify(userRepository)
                .findByEmail(any());

        verify(passwordEncoder)
                .matches(
                        input.password(),
                        user.getPassword()
                );

        verify(jwtProvider)
                .generateToken(
                        user.getId().toString(),
                        user.getEmail().getValue(),
                        user.getRole().name()
                );
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        LoginInput input = LoginInputTestBuilder.builder().build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> useCase.execute(input)
                );

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtProvider);

        assertEquals(
                "Invalid credentials",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        User user = UserTestBuilder.builder().build();
        LoginInput input = LoginInputTestBuilder.builder().build();

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
            input.password(),
            user.getPassword()
        )).thenReturn(false);

        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () -> useCase.execute(input)
                );

        verifyNoInteractions(jwtProvider);

        assertEquals(
                "Invalid credentials",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        LoginInput input =
                LoginInputTestBuilder.builder()
                        .withEmail("invalid-email")
                        .build();

        InvalidUserException exception =
                assertThrows(
                        InvalidUserException.class,
                        () -> useCase.execute(input)
                );

        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtProvider);

        assertEquals(
                "Invalid email",
                exception.getMessage()
        );
    }
}

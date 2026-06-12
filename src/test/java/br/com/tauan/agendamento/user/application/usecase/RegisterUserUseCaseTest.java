package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.JwtProvider;
import br.com.tauan.agendamento.test.factory.UserInputTestBuilder;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.user.application.dto.AuthOutput;
import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.application.dto.UserOutput;
import br.com.tauan.agendamento.user.application.exception.EmailAlreadyExistsException;
import br.com.tauan.agendamento.user.application.mapper.UserMapper;
import br.com.tauan.agendamento.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void shouldRegisterUserAndReturnToken() {
        User user = UserTestBuilder.builder().build();

        UserOutput userOutput = UserMapper.toOutput(user);

        CreateUserInput input =
                UserInputTestBuilder.builder().build();

        when(createUserUseCase.execute(input))
                .thenReturn(userOutput);

        when(jwtProvider.generateToken(
                userOutput.id().toString(),
                userOutput.email(),
                userOutput.role()
        )).thenReturn("jwt-token");

        AuthOutput output = useCase.execute(input);

        assertEquals(
                "jwt-token",
                output.token()
        );

        verify(createUserUseCase)
                .execute(input);

        verify(jwtProvider)
                .generateToken(
                        userOutput.id().toString(),
                        userOutput.email(),
                        userOutput.role()
                );
    }

    @Test
    void shouldPropagateExceptionWhenCreateUserFails() {
        CreateUserInput input =
                UserInputTestBuilder.builder().build();

        when(createUserUseCase.execute(input))
                .thenThrow(new EmailAlreadyExistsException());

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> useCase.execute(input)
        );

        verifyNoInteractions(jwtProvider);
    }
}

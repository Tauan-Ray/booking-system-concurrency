package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.JwtProvider;
import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.application.dto.AuthOutput;
import br.com.tauan.agendamento.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final JwtProvider jwtProvider;

    public AuthOutput execute(CreateUserInput input) {
        User user = createUserUseCase.execute(input);

        String token = jwtProvider.generateToken(
                user.getId().toString(),
                user.getEmail().getValue(),
                user.getRole().name()
        );

        return new AuthOutput(token);
    }
}

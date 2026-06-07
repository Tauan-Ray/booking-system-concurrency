package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.JwtProvider;
import br.com.tauan.agendamento.shared.application.contract.PasswordEncoder;
import br.com.tauan.agendamento.user.application.dto.LoginInput;
import br.com.tauan.agendamento.user.application.dto.AuthOutput;
import br.com.tauan.agendamento.user.application.exception.InvalidCredentialsException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import br.com.tauan.agendamento.user.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthOutput execute(LoginInput input) {
        User user = userRepository.findByEmail(new Email(input.email()))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(input.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtProvider.generateToken(
                user.getId().toString(),
                user.getEmail().getValue(),
                user.getRole().name()
        );

        return new AuthOutput(token);
    }
}

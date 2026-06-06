package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.PasswordEncoder;
import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.application.exception.EmailAlreadyExistsException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import br.com.tauan.agendamento.user.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(CreateUserInput input) {
        Email email = new Email(input.email());

        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException();
                });

        String encodedPassword = passwordEncoder.encode(input.password());

        User user = User.create(input.name(), email, encodedPassword);

        return userRepository.save(user);
    }
}

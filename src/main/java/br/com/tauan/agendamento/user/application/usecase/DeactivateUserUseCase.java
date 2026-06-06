package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.user.application.exception.UserNotFoundException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateUserUseCase {

    private final UserRepository userRepository;

    public void execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.deactivate();

        userRepository.save(user);
    }
}

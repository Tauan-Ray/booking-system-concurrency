package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.user.application.exception.UserNotFoundException;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByIdIncludingDeletedUseCase {

    private final UserRepository userRepository;

    public User execute(UUID id) {
        return userRepository.findByIdIncludingDeleted(id)
                .orElseThrow(UserNotFoundException::new);
    }
}

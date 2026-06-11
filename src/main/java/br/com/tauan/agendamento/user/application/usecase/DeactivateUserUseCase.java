package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
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
    private final AuthenticatedUserProvider auth;

    public void execute(UUID id) {
        UUID requesterId = auth.getUserId();
        boolean isOwner = requesterId.equals(id);
        boolean isAdmin = auth.hasRole("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException();
        }

        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.deactivate();

        userRepository.save(user);
    }
}

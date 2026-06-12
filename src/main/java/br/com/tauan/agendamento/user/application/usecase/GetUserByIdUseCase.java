package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import br.com.tauan.agendamento.user.application.dto.UserOutput;
import br.com.tauan.agendamento.user.application.exception.UserNotFoundException;
import br.com.tauan.agendamento.user.application.mapper.UserMapper;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;
    private final AuthenticatedUserProvider auth;

    public UserOutput execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        UUID requesterId = auth.getUserId();
        boolean isOwner = requesterId.equals(id);
        boolean isAdmin = auth.hasRole("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException();
        }

        return UserMapper.toOutput(user);
    }
}

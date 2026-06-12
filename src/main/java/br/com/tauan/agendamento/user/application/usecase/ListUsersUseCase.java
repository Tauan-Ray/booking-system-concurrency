package br.com.tauan.agendamento.user.application.usecase;

import br.com.tauan.agendamento.user.application.dto.UserOutput;
import br.com.tauan.agendamento.user.application.mapper.UserMapper;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserRepository userRepository;

    public List<UserOutput> execute() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toOutput)
                .toList();
    }
}

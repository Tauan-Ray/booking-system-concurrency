package br.com.tauan.agendamento.user.presentation.mapper;

import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;

import java.util.List;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public static CreateUserInput toInput(CreateUserRequest request) {
        return new CreateUserInput(
                request.name(),
                request.email(),
                request.password()
        );
    }
}

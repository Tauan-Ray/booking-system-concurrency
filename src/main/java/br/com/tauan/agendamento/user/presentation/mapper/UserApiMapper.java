package br.com.tauan.agendamento.user.presentation.mapper;

import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.application.dto.UserOutput;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;

import java.util.List;

public class UserApiMapper {
    public static UserResponse toResponse(UserOutput output) {
        return new UserResponse(
                output.id(),
                output.name(),
                output.email(),
                output.role(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static List<UserResponse> toResponseList(List<UserOutput> outputs) {
        return outputs.stream()
                .map(UserApiMapper::toResponse)
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

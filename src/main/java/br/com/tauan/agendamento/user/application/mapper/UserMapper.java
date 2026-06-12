package br.com.tauan.agendamento.user.application.mapper;

import br.com.tauan.agendamento.user.application.dto.UserOutput;
import br.com.tauan.agendamento.user.domain.entity.User;

public class UserMapper {
    public static UserOutput toOutput(User user) {
        return new UserOutput(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }
}

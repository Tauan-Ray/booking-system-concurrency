package br.com.tauan.agendamento.user.presentation.docs;

import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "UserListApiResponse", description = "Envelope de sucesso contendo uma lista de usuários.")
public record UserListApiResponse(
        @Schema(example = "true")
        boolean success,

        List<UserResponse> data
) {}

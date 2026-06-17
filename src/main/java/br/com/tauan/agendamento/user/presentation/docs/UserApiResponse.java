package br.com.tauan.agendamento.user.presentation.docs;

import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserApiResponse", description = "Envelope de sucesso contendo um usuário.")
public record UserApiResponse(
        @Schema(example = "true")
        boolean success,

        UserResponse data
) {}

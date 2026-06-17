package br.com.tauan.agendamento.user.presentation.docs;

import br.com.tauan.agendamento.user.presentation.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthApiResponse", description = "Envelope de sucesso contendo o token de autenticação.")
public record AuthApiResponse(
        @Schema(example = "true")
        boolean success,

        AuthResponse data
) {}

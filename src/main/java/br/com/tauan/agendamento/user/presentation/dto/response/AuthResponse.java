package br.com.tauan.agendamento.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo o token JWT.")
public record AuthResponse(
        @Schema(
                description = "Token JWT a ser enviado no header Authorization como 'Bearer <token>'.",
                example = "eyJhbGciOiJIUzI1Ni1mb..."
        )
        String token
) {}

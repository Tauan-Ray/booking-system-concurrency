package br.com.tauan.agendamento.user.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Representação de um usuário.")
public record UserResponse(
        @Schema(description = "Identificador único do usuário.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Nome completo do usuário.", example = "Maria Silva")
        String name,

        @Schema(description = "E-mail do usuário.", example = "maria.silva@email.com")
        String email,

        @Schema(description = "Papel do usuário no sistema.", example = "USER", allowableValues = {"USER", "ADMIN"})
        String role,

        @Schema(description = "Data/hora de criação do registro.")
        LocalDateTime createdAt,

        @Schema(description = "Data/hora da última atualização.")
        LocalDateTime updatedAt,

        @Schema(description = "Data/hora de desativação (soft delete). Nulo se ativo.")
        LocalDateTime deletedAt
) {}

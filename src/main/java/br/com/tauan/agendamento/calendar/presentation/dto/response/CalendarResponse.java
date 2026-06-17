package br.com.tauan.agendamento.calendar.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Representação de uma agenda (calendar).")
public record CalendarResponse(
        @Schema(description = "Identificador único da agenda.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Nome da agenda.", example = "Quadra de Tênis 1")
        String name,

        @Schema(description = "Data/hora de criação do registro.")
        LocalDateTime createdAt,

        @Schema(description = "Data/hora da última atualização.")
        LocalDateTime updatedAt,

        @Schema(description = "Data/hora do arquivamento (soft delete). Nulo se ativa.")
        LocalDateTime deletedAt
) {}

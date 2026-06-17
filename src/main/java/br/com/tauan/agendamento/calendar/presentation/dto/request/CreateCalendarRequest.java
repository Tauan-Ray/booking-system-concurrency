package br.com.tauan.agendamento.calendar.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados necessários para criar uma agenda (calendar).")
public record CreateCalendarRequest(
        @Schema(description = "Nome da agenda.", example = "Quadra de Tênis 1")
        @NotBlank
        @NotNull
        String name
) {}

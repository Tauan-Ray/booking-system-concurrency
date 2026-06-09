package br.com.tauan.agendamento.calendar.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCalendarRequest(
        @NotBlank
        @NotNull
        String name
) {}

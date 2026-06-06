package br.com.tauan.agendamento.shared.presentation.dto.response;

public record ErrorResponse(
        String message,
        int status,
        String error
) {}

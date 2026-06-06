package br.com.tauan.agendamento.shared.presentation.dto.response;

import java.util.List;

public record ErrorResponse(
        String message,
        int status,
        String code,
        List<FieldErrorResponse> fieldErrors
) {}

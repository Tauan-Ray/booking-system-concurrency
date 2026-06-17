package br.com.tauan.agendamento.shared.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Erro de validação associado a um campo específico da requisição.")
public record FieldErrorResponse(
        @Schema(description = "Nome do campo que falhou na validação.", example = "email")
        String field,

        @Schema(description = "Mensagem explicando o motivo da falha.", example = "must not be blank")
        String message
) {}

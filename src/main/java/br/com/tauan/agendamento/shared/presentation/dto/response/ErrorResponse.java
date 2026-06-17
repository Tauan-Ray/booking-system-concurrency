package br.com.tauan.agendamento.shared.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Detalhes de um erro retornado pela API.")
public record ErrorResponse(
        @Schema(description = "Mensagem legível descrevendo o erro.", example = "Validation failed")
        String message,

        @Schema(description = "Código HTTP correspondente ao erro.", example = "400")
        int status,

        @Schema(description = "Código interno que identifica o tipo de erro.", example = "VALIDATION_ERROR")
        String code,

        @Schema(description = "Lista de erros por campo, presente em falhas de validação.")
        List<FieldErrorResponse> fieldErrors
) {}

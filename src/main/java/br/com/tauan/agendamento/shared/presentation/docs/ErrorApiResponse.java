package br.com.tauan.agendamento.shared.presentation.docs;

import br.com.tauan.agendamento.shared.presentation.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ErrorApiResponse",
        description = "Envelope retornado quando a operação falha. O campo 'data' é sempre nulo."
)
public record ErrorApiResponse(
        @Schema(description = "Sempre false em respostas de erro.", example = "false")
        boolean success,

        @Schema(description = "Detalhes do erro.")
        ErrorResponse error
) {}

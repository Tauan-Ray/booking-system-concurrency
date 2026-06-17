package br.com.tauan.agendamento.shared.presentation.docs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "EmptyApiResponse",
        description = "Envelope de sucesso sem corpo de dados (usado em operações como exclusão/arquivamento)."
)
public record EmptyApiResponse(
        @Schema(description = "Sempre true em respostas de sucesso.", example = "true")
        boolean success
) {}

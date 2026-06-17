package br.com.tauan.agendamento.shared.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Envelope padrão de todas as respostas da API.")
public record ApiResponse<T>(
        @Schema(description = "Indica se a operação foi bem-sucedida.", example = "true")
        boolean success,

        @Schema(description = "Carga útil da resposta. Nulo quando ocorre um erro.")
        T data,

        @Schema(description = "Detalhes do erro. Nulo quando a operação é bem-sucedida.")
        ErrorResponse error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error);
    }
}

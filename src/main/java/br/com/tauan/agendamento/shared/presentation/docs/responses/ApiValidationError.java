package br.com.tauan.agendamento.shared.presentation.docs.responses;

import br.com.tauan.agendamento.shared.presentation.docs.ApiExamples;
import br.com.tauan.agendamento.shared.presentation.docs.ErrorApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "400", description = "Dados inválidos na requisição",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorApiResponse.class),
                examples = @ExampleObject(value = ApiExamples.VALIDATION_ERROR)))
public @interface ApiValidationError {
}

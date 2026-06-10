package br.com.tauan.agendamento.shared.presentation.advice;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.shared.presentation.dto.response.ErrorResponse;
import br.com.tauan.agendamento.shared.presentation.dto.response.FieldErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Object>> handleDomainException(DomainException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                ex.getStatus(),
                ex.getCode(),
                List.of()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorResponse(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        ErrorResponse error = new ErrorResponse(
                "Validation failed",
                400,
                "VALIDATION_ERROR",
                fieldErrors
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidFormat(
            HttpMessageNotReadableException ex
    ) {

        List<FieldErrorResponse> fieldErrors = List.of();

        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {

            String field = invalidFormat.getPath()
                    .stream()
                    .findFirst()
                    .map(JsonMappingException.Reference::getFieldName)
                    .orElse("unknown");


            fieldErrors = List.of(
                    new FieldErrorResponse(
                            field,
                            "Invalid value"
                    )
            );
        }

        ErrorResponse error = new ErrorResponse(
                "Validation failed",
                400,
                "VALIDATION_ERROR",
                fieldErrors
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(error));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {

        List<FieldErrorResponse> fieldErrors = List.of(
                new FieldErrorResponse(
                        ex.getName(),
                        "Invalid value"
                )
        );

        ErrorResponse error = new ErrorResponse(
                "Validation failed",
                400,
                "VALIDATION_ERROR",
                fieldErrors
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(error));
    }
}

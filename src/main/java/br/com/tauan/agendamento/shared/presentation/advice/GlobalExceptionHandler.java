package br.com.tauan.agendamento.shared.presentation.advice;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.shared.presentation.dto.response.ErrorResponse;
import br.com.tauan.agendamento.shared.presentation.dto.response.FieldErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}

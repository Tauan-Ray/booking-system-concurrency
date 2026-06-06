package br.com.tauan.agendamento.shared.presentation.advice;

import br.com.tauan.agendamento.shared.domain.exception.DomainException;
import br.com.tauan.agendamento.shared.presentation.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(
                        ex.getMessage(),
                        ex.getStatus(),
                        ex.getCode()
                ));
    }
}

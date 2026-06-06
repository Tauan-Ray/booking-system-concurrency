package br.com.tauan.agendamento.user.presentation.controller;

import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.user.application.dto.LoginOutput;
import br.com.tauan.agendamento.user.application.usecase.LoginUseCase;
import br.com.tauan.agendamento.user.presentation.dto.request.LoginRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.LoginResponse;
import br.com.tauan.agendamento.user.presentation.mapper.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginOutput output = loginUseCase.execute(
                AuthMapper.toInput(request)
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        AuthMapper.toResponse(output)
                )
        );
    }
}

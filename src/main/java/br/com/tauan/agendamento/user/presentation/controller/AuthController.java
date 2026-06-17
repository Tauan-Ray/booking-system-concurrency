package br.com.tauan.agendamento.user.presentation.controller;

import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.user.application.dto.AuthOutput;
import br.com.tauan.agendamento.user.application.usecase.LoginUseCase;
import br.com.tauan.agendamento.user.application.usecase.RegisterUserUseCase;
import br.com.tauan.agendamento.user.presentation.docs.AuthControllerDocs;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.request.LoginRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.AuthResponse;
import br.com.tauan.agendamento.user.presentation.mapper.AuthApiMapper;
import br.com.tauan.agendamento.user.presentation.mapper.UserApiMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    @Override
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody CreateUserRequest request
    ) {
        AuthOutput output = registerUserUseCase.execute(UserApiMapper.toInput(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(AuthApiMapper.toResponse(output)));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthOutput output = loginUseCase.execute(
                AuthApiMapper.toInput(request)
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        AuthApiMapper.toResponse(output)
                )
        );
    }
}

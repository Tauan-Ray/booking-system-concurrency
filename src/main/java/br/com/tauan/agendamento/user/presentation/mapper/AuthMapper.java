package br.com.tauan.agendamento.user.presentation.mapper;


import br.com.tauan.agendamento.user.application.dto.LoginInput;
import br.com.tauan.agendamento.user.application.dto.AuthOutput;
import br.com.tauan.agendamento.user.presentation.dto.request.LoginRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.AuthResponse;

public class AuthMapper {

    public static AuthResponse toResponse(AuthOutput output) {
        return new AuthResponse(output.token());
    }

    public static LoginInput toInput(LoginRequest request) {
        return new LoginInput(
                request.email(),
                request.password()
        );
    }

}

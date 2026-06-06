package br.com.tauan.agendamento.user.presentation.mapper;


import br.com.tauan.agendamento.user.application.dto.LoginInput;
import br.com.tauan.agendamento.user.application.dto.LoginOutput;
import br.com.tauan.agendamento.user.presentation.dto.request.LoginRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.LoginResponse;

public class AuthMapper {

    public static LoginResponse toResponse(LoginOutput output) {
        return new LoginResponse(output.token());
    }

    public static LoginInput toInput(LoginRequest request) {
        return new LoginInput(
                request.email(),
                request.password()
        );
    }

}

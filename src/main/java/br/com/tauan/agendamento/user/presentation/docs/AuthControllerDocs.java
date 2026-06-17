package br.com.tauan.agendamento.user.presentation.docs;

import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiEmailConflict;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiInvalidCredentials;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiValidationError;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.request.LoginRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Registro e autenticação de usuários. Endpoints públicos que emitem o token JWT.")
public interface AuthControllerDocs {

    @Operation(
            summary = "Registra um novo usuário",
            description = "Cria um usuário com papel USER e retorna um token JWT já autenticado. Endpoint público."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", description = "Usuário criado e autenticado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthApiResponse.class)))
    @ApiValidationError
    @ApiEmailConflict
    ResponseEntity<ApiResponse<AuthResponse>> register(CreateUserRequest request);

    @Operation(
            summary = "Autentica um usuário",
            description = "Valida as credenciais e retorna um token JWT para acesso aos endpoints protegidos. Endpoint público."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Autenticação realizada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthApiResponse.class)))
    @ApiValidationError
    @ApiInvalidCredentials
    ResponseEntity<ApiResponse<AuthResponse>> login(LoginRequest request);
}

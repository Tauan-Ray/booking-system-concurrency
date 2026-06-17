package br.com.tauan.agendamento.user.presentation.docs;

import br.com.tauan.agendamento.shared.presentation.docs.EmptyApiResponse;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiEmailConflict;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiForbidden;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiUnauthenticated;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiUserNotFound;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiValidationError;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.user.presentation.dto.request.CreateUserRequest;
import br.com.tauan.agendamento.user.presentation.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "Gestão de usuários. Requer autenticação; algumas operações são restritas a ADMIN.")
@SecurityRequirement(name = "bearerAuth")
public interface UserControllerDocs {

    @Operation(
            summary = "Lista todos os usuários",
            description = "Retorna todos os usuários cadastrados. Restrito a usuários com papel ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserListApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    ResponseEntity<ApiResponse<List<UserResponse>>> listAllUsers();

    @Operation(
            summary = "Busca um usuário por ID",
            description = "Retorna os dados de um usuário específico. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Usuário encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserApiResponse.class)))
    @ApiUnauthenticated
    @ApiUserNotFound
    ResponseEntity<ApiResponse<UserResponse>> findById(
            @Parameter(description = "Identificador do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );

    @Operation(
            summary = "Cria um usuário",
            description = "Cria um novo usuário. Restrito a usuários com papel ADMIN. Diferente de /auth/register, não retorna token."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", description = "Usuário criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserApiResponse.class)))
    @ApiValidationError
    @ApiUnauthenticated
    @ApiForbidden
    @ApiEmailConflict
    ResponseEntity<ApiResponse<UserResponse>> createUser(CreateUserRequest request);

    @Operation(
            summary = "Desativa um usuário",
            description = "Realiza o soft delete (desativação) de um usuário. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Usuário desativado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmptyApiResponse.class)))
    @ApiUnauthenticated
    @ApiUserNotFound
    ResponseEntity<ApiResponse<Void>> deactivate(
            @Parameter(description = "Identificador do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );
}

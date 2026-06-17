package br.com.tauan.agendamento.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para criar/registrar um usuário.")
public record CreateUserRequest(
        @Schema(description = "Nome completo do usuário.", example = "Maria Silva", minLength = 3, maxLength = 100)
        @NotBlank
        @Size(min = 3, max = 100)
        String name,

        @Schema(description = "E-mail único do usuário, usado também para login.", example = "maria.silva@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha de acesso.", example = "senha123", minLength = 6, maxLength = 50)
        @NotBlank
        @Size(min = 6, max = 50)
        String password
) {}

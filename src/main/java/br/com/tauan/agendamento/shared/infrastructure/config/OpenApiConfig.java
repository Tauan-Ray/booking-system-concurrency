package br.com.tauan.agendamento.shared.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Agendamento",
                version = "v1",
                description = """
                        API REST para gerenciamento de agendamentos baseada em Clean Architecture.

                        ## Domínios
                        - **Auth**: registro e autenticação de usuários (emissão de JWT).
                        - **Users**: gestão de usuários (ações administrativas e consulta).
                        - **Calendars**: agendas que agrupam horários disponíveis.
                        - **TimeSlots**: faixas de horário pertencentes a uma agenda.
                        - **Reservations**: reservas de horários. É o núcleo do projeto, onde o controle de
                          concorrência (locking) garante que um mesmo horário não seja reservado duas vezes
                          para a mesma data.

                        ## Autenticação
                        Os endpoints (exceto `/auth/**`) exigem um token JWT no header
                        `Authorization: Bearer <token>`. Obtenha o token em `POST /auth/login` ou
                        `POST /auth/register` e clique em **Authorize** para usá-lo nas requisições.

                        ## Formato das respostas
                        Todas as respostas seguem o envelope `ApiResponse`, com os campos `success`, `data`
                        e `error`. Em caso de erro, `data` é nulo e `error` traz `message`, `status`, `code`
                        e, quando aplicável, `fieldErrors`.
                        """,
                contact = @Contact(name = "Tauan"),
                license = @License(name = "MIT")
        ),
        servers = {
                @Server(url = "/", description = "Servidor padrão")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Informe o token JWT obtido no login (sem o prefixo 'Bearer ').",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

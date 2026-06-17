package br.com.tauan.agendamento.reservation.presentation.docs;

import br.com.tauan.agendamento.reservation.presentation.dto.request.CreateReservationRequest;
import br.com.tauan.agendamento.reservation.presentation.dto.response.ReservationResponse;
import br.com.tauan.agendamento.shared.presentation.docs.ApiExamples;
import br.com.tauan.agendamento.shared.presentation.docs.EmptyApiResponse;
import br.com.tauan.agendamento.shared.presentation.docs.ErrorApiResponse;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiForbidden;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiReservationAlreadyCancelled;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiReservationConflict;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiReservationNotFound;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiUnauthenticated;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiValidationError;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Reservations",
        description = """
                Reservas de horários — núcleo do projeto. A criação de reservas usa controle de
                concorrência (locking) para impedir que o mesmo time slot seja reservado duas vezes
                na mesma data. Requer autenticação; algumas consultas são restritas a ADMIN.
                """
)
@SecurityRequirement(name = "bearerAuth")
public interface ReservationControllerDocs {

    @Operation(
            summary = "Lista todas as reservas",
            description = "Retorna todas as reservas cadastradas. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationListApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    ResponseEntity<ApiResponse<List<ReservationResponse>>> listAllReservations();

    @Operation(
            summary = "Lista reservas por usuário",
            description = "Retorna as reservas de um usuário específico. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationListApiResponse.class)))
    @ApiUnauthenticated
    ResponseEntity<ApiResponse<List<ReservationResponse>>> findByUserId(
            @Parameter(description = "Identificador do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID userId
    );

    @Operation(
            summary = "Lista reservas por faixa de horário",
            description = "Retorna as reservas associadas a um time slot específico. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationListApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    ResponseEntity<ApiResponse<List<ReservationResponse>>> findByTimeSlotId(
            @Parameter(description = "Identificador do time slot", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID timeSlotId
    );

    @Operation(
            summary = "Busca uma reserva por ID",
            description = "Retorna os dados de uma reserva específica. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Reserva encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationApiResponse.class)))
    @ApiUnauthenticated
    @ApiReservationNotFound
    ResponseEntity<ApiResponse<ReservationResponse>> findById(
            @Parameter(description = "Identificador da reserva", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );

    @Operation(
            summary = "Cria uma reserva",
            description = """
                    Reserva um time slot para uma data. O fluxo usa controle de concorrência (locking)
                    para garantir que o mesmo horário não seja reservado em duplicidade na mesma data:
                    requisições concorrentes para o mesmo slot/data resultam em conflito (409).
                    Requer autenticação.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", description = "Reserva criada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReservationApiResponse.class)))
    @ApiValidationError
    @ApiUnauthenticated
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "Time slot ou usuário não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorApiResponse.class),
                    examples = @ExampleObject(value = ApiExamples.TIME_SLOT_NOT_FOUND)))
    @ApiReservationConflict
    ResponseEntity<ApiResponse<ReservationResponse>> createReservation(CreateReservationRequest request);

    @Operation(
            summary = "Cancela uma reserva",
            description = "Altera o status da reserva para CANCELLED, liberando o horário. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Reserva cancelada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmptyApiResponse.class)))
    @ApiUnauthenticated
    @ApiReservationNotFound
    @ApiReservationAlreadyCancelled
    ResponseEntity<ApiResponse<Void>> cancelReservation(
            @Parameter(description = "Identificador da reserva", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );
}

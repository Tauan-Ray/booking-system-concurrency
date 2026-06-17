package br.com.tauan.agendamento.timeslot.presentation.docs;

import br.com.tauan.agendamento.shared.presentation.docs.EmptyApiResponse;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiCalendarNotFound;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiForbidden;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiTimeSlotConflict;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiTimeSlotNotFound;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiUnauthenticated;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiValidationError;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.timeslot.presentation.dto.request.CreateTimeSlotRequest;
import br.com.tauan.agendamento.timeslot.presentation.dto.response.TimeSlotResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "TimeSlots", description = "Faixas de horário das agendas. Consultas exigem autenticação; criação e arquivamento são restritos a ADMIN.")
@SecurityRequirement(name = "bearerAuth")
public interface TimeSlotControllerDocs {

    @Operation(
            summary = "Lista todas as faixas de horário",
            description = "Retorna todos os time slots cadastrados. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeSlotListApiResponse.class)))
    @ApiUnauthenticated
    ResponseEntity<ApiResponse<List<TimeSlotResponse>>> listAllTimeSlots();

    @Operation(
            summary = "Lista faixas de horário por agenda",
            description = "Retorna os time slots pertencentes a uma agenda específica. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeSlotListApiResponse.class)))
    @ApiUnauthenticated
    ResponseEntity<ApiResponse<List<TimeSlotResponse>>> findByCalendarId(
            @Parameter(description = "Identificador da agenda", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID calendarId
    );

    @Operation(
            summary = "Busca uma faixa de horário por ID",
            description = "Retorna os dados de um time slot específico. Requer autenticação."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Time slot encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeSlotApiResponse.class)))
    @ApiUnauthenticated
    @ApiTimeSlotNotFound
    ResponseEntity<ApiResponse<TimeSlotResponse>> findById(
            @Parameter(description = "Identificador do time slot", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );

    @Operation(
            summary = "Cria uma faixa de horário",
            description = "Cria um novo time slot em uma agenda. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", description = "Time slot criado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeSlotApiResponse.class)))
    @ApiValidationError
    @ApiUnauthenticated
    @ApiForbidden
    @ApiCalendarNotFound
    @ApiTimeSlotConflict
    ResponseEntity<ApiResponse<TimeSlotResponse>> createTimeSlot(CreateTimeSlotRequest request);

    @Operation(
            summary = "Arquiva uma faixa de horário",
            description = "Realiza o soft delete (arquivamento) de um time slot. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Time slot arquivado com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmptyApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    @ApiTimeSlotNotFound
    ResponseEntity<ApiResponse<Void>> archiveTimeSlot(
            @Parameter(description = "Identificador do time slot", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );
}

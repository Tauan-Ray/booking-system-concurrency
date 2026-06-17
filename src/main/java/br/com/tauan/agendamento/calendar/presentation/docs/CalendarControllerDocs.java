package br.com.tauan.agendamento.calendar.presentation.docs;

import br.com.tauan.agendamento.calendar.presentation.dto.request.CreateCalendarRequest;
import br.com.tauan.agendamento.calendar.presentation.dto.response.CalendarResponse;
import br.com.tauan.agendamento.shared.presentation.docs.EmptyApiResponse;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiCalendarConflict;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiCalendarNotFound;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiForbidden;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiUnauthenticated;
import br.com.tauan.agendamento.shared.presentation.docs.responses.ApiValidationError;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Calendars", description = "Gestão de agendas. Todas as operações são restritas a usuários ADMIN.")
@SecurityRequirement(name = "bearerAuth")
public interface CalendarControllerDocs {

    @Operation(
            summary = "Lista todas as agendas",
            description = "Retorna todas as agendas cadastradas. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalendarListApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    ResponseEntity<ApiResponse<List<CalendarResponse>>> listAllCalendars();

    @Operation(
            summary = "Busca uma agenda por ID",
            description = "Retorna os dados de uma agenda específica. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Agenda encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalendarApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    @ApiCalendarNotFound
    ResponseEntity<ApiResponse<CalendarResponse>> findById(
            @Parameter(description = "Identificador da agenda", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );

    @Operation(
            summary = "Cria uma agenda",
            description = "Cria uma nova agenda. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", description = "Agenda criada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalendarApiResponse.class)))
    @ApiValidationError
    @ApiUnauthenticated
    @ApiForbidden
    @ApiCalendarConflict
    ResponseEntity<ApiResponse<CalendarResponse>> createCalendar(CreateCalendarRequest request);

    @Operation(
            summary = "Arquiva uma agenda",
            description = "Realiza o soft delete (arquivamento) de uma agenda. Restrito a ADMIN."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Agenda arquivada com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmptyApiResponse.class)))
    @ApiUnauthenticated
    @ApiForbidden
    @ApiCalendarNotFound
    ResponseEntity<ApiResponse<Void>> archiveCalendar(
            @Parameter(description = "Identificador da agenda", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID id
    );
}

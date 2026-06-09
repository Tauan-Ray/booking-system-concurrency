package br.com.tauan.agendamento.calendar.presentation.controller;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.usecase.ArchiveCalendarUseCase;
import br.com.tauan.agendamento.calendar.application.usecase.CreateCalendarUseCase;
import br.com.tauan.agendamento.calendar.application.usecase.GetCalendarByIdUseCase;
import br.com.tauan.agendamento.calendar.application.usecase.ListCalendarsUseCase;
import br.com.tauan.agendamento.calendar.presentation.dto.response.CalendarResponse;
import br.com.tauan.agendamento.calendar.presentation.dto.request.CreateCalendarRequest;
import br.com.tauan.agendamento.calendar.presentation.mapper.CalendarMapper;
import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        value = "/calendars",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CalendarController {

    private final ListCalendarsUseCase listCalendarsUseCase;
    private final GetCalendarByIdUseCase getCalendarByIdUseCase;
    private final CreateCalendarUseCase createCalendarUseCase;
    private final ArchiveCalendarUseCase archiveCalendarUseCase;


    @GetMapping
    public ResponseEntity<ApiResponse<List<CalendarResponse>>> listAllCalendars() {
        List<CalendarOutput> calendars = listCalendarsUseCase.execute();

        List<CalendarResponse> calendarsResponse =
                calendars.stream()
                        .map(CalendarMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.success(calendarsResponse)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CalendarResponse>> findById(
            @PathVariable UUID id
    ) {
        CalendarOutput calendar = getCalendarByIdUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(CalendarMapper.toResponse(calendar))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CalendarResponse>> createCalendar(
            @Valid @RequestBody CreateCalendarRequest request
    ) {
        CalendarOutput calendar = createCalendarUseCase.execute(
                CalendarMapper.toInput(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(CalendarMapper.toResponse(calendar)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> archiveCalendar(
            @PathVariable UUID id
    ) {
        archiveCalendarUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}

package br.com.tauan.agendamento.timeslot.presentation.controller;

import br.com.tauan.agendamento.shared.presentation.dto.response.ApiResponse;
import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.usecase.*;
import br.com.tauan.agendamento.timeslot.presentation.dto.request.CreateTimeSlotRequest;
import br.com.tauan.agendamento.timeslot.presentation.dto.response.TimeSlotResponse;
import br.com.tauan.agendamento.timeslot.presentation.mapper.TimeSlotApiMapper;
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
        value = "/timeslots",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class TimeSlotController {

    private final ListTimeSlotsUseCase listTimeSlotsUseCase;
    private final GetTimeSlotByIdUseCase getTimeSlotByIdUseCase;
    private final GetTimeSlotByIdIncludingDeletedUseCase getTimeSlotByIdIncludingDeletedUseCase;
    private final CreateTimeSlotUseCase createTimeSlotUseCase;
    private final ArchiveTimeSlotUseCase archiveTimeSlotUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TimeSlotResponse>>> listAllTimeSlots() {
        List<TimeSlotOutput> timeSlots = listTimeSlotsUseCase.execute();

        List<TimeSlotResponse> timeSlotResponses =
                timeSlots.stream()
                        .map(TimeSlotApiMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.success(timeSlotResponses)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TimeSlotResponse>> findById(
            @PathVariable UUID id
    ) {
        TimeSlotOutput timeSlot = getTimeSlotByIdUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(TimeSlotApiMapper.toResponse(timeSlot))
        );
    }

    @GetMapping("/{id}/including-deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TimeSlotResponse>> findByIdIncludingDeleted(
            @PathVariable UUID id
    ) {
        TimeSlotOutput timeSlot = getTimeSlotByIdIncludingDeletedUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(TimeSlotApiMapper.toResponse(timeSlot))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TimeSlotResponse>> createTimeSlot(
            @Valid @RequestBody CreateTimeSlotRequest request
    ) {
        TimeSlotOutput timeSlot = createTimeSlotUseCase.execute(
                TimeSlotApiMapper.toInput(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(TimeSlotApiMapper.toResponse(timeSlot)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> archiveTimeSlot(
            @PathVariable UUID id
    ) {
        archiveTimeSlotUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}

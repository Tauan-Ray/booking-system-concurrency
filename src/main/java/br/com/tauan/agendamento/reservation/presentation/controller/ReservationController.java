package br.com.tauan.agendamento.reservation.presentation.controller;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.usecase.*;
import br.com.tauan.agendamento.reservation.presentation.docs.ReservationControllerDocs;
import br.com.tauan.agendamento.reservation.presentation.dto.request.CreateReservationRequest;
import br.com.tauan.agendamento.reservation.presentation.dto.response.ReservationResponse;
import br.com.tauan.agendamento.reservation.presentation.mapper.ReservationApiMapper;
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
        value = "/reservations",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class ReservationController implements ReservationControllerDocs {

    private final ListReservationsUseCase listReservationsUseCase;
    private final ListReservationsByUserUseCase listReservationsByUserUseCase;
    private final ListReservationsByTimeSlotUseCase listReservationsByTimeSlotUseCase;
    private final GetReservationByIdUseCase getReservationByIdUseCase;
    private final CreateReservationUseCase createReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;

    @Override
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> listAllReservations() {
        List<ReservationOutput> reservations = listReservationsUseCase.execute();

        return ResponseEntity.ok(
                ApiResponse.success(ReservationApiMapper.toResponseList(reservations))
        );
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> findByUserId(@PathVariable UUID userId) {
        List<ReservationOutput> reservations = listReservationsByUserUseCase.execute(userId);

        return ResponseEntity.ok(
                ApiResponse.success(ReservationApiMapper.toResponseList(reservations))
        );
    }

    @Override
    @GetMapping("/timeslot/{timeSlotId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> findByTimeSlotId(@PathVariable UUID timeSlotId) {
        List<ReservationOutput> reservations =
                listReservationsByTimeSlotUseCase.execute(timeSlotId);

        return ResponseEntity.ok(
                ApiResponse.success(ReservationApiMapper.toResponseList(reservations))
        );
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> findById(@PathVariable UUID id) {
        ReservationOutput reservation = getReservationByIdUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(ReservationApiMapper.toResponse(reservation))
        );
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        ReservationOutput reservation = createReservationUseCase.execute(
                ReservationApiMapper.toInput(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(ReservationApiMapper.toResponse(reservation)));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable UUID id) {
        cancelReservationUseCase.execute(id);

        return ResponseEntity.ok(
                ApiResponse.success(null)
        );
    }
}

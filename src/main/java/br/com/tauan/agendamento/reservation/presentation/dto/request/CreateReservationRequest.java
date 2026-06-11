package br.com.tauan.agendamento.reservation.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateReservationRequest(
        UUID userId,

        @NotNull
        UUID timeSlotId,

        @NotNull
        LocalDate reservationDate
) {}

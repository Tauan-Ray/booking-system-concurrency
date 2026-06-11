package br.com.tauan.agendamento.reservation.presentation.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID userId,
        UUID timeSlotId,
        String status,
        LocalDate reservationDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

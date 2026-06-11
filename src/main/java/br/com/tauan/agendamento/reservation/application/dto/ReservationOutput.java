package br.com.tauan.agendamento.reservation.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationOutput(
        UUID id,
        UUID userId,
        UUID timeSlotId,
        String status,
        LocalDate reservationDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

package br.com.tauan.agendamento.reservation.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CreateReservationInput(
        UUID userId,
        UUID timeSlotId,
        LocalDate reservationDate
) {}

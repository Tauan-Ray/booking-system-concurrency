package br.com.tauan.agendamento.reservation.presentation.mapper;

import br.com.tauan.agendamento.reservation.application.dto.CreateReservationInput;
import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.presentation.dto.request.CreateReservationRequest;
import br.com.tauan.agendamento.reservation.presentation.dto.response.ReservationResponse;

public class ReservationApiMapper {
    public static ReservationResponse toResponse(ReservationOutput output) {
        return new ReservationResponse(
                output.id(),
                output.userId(),
                output.timeSlotId(),
                output.status(),
                output.reservationDate(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    public static CreateReservationInput toInput(CreateReservationRequest request) {
        return new CreateReservationInput(
                request.userId(),
                request.timeSlotId(),
                request.reservationDate()
        );
    }
}

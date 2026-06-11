package br.com.tauan.agendamento.reservation.application.mapper;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;

public class ReservationMapper {

    public static ReservationOutput toOutput(Reservation reservation) {
        return new ReservationOutput(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getTimeSlotId(),
                reservation.getStatus().name(),
                reservation.getReservationDate(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}

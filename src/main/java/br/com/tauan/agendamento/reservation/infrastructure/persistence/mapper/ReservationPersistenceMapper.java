package br.com.tauan.agendamento.reservation.infrastructure.persistence.mapper;

import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.infrastructure.persistence.entity.ReservationJpaEntity;

public class ReservationPersistenceMapper {
    public static Reservation toDomain(ReservationJpaEntity entity) {
        return Reservation.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getTimeSlotId(),
                entity.getReservationDate(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public static ReservationJpaEntity toJpaEntity(Reservation reservation) {
        ReservationJpaEntity entity = new ReservationJpaEntity();

        entity.setId(reservation.getId());
        entity.setUserId(reservation.getUserId());
        entity.setTimeSlotId(reservation.getTimeSlotId());
        entity.setReservationDate(reservation.getReservationDate());
        entity.setStatus(reservation.getStatus());
        entity.setCreatedAt(reservation.getCreatedAt());
        entity.setUpdatedAt(reservation.getUpdatedAt());
        entity.setDeletedAt(reservation.getDeletedAt());

        return entity;
    }
}

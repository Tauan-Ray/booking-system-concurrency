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

        entity.setId(entity.getId());
        entity.setUserId(entity.getUserId());
        entity.setTimeSlotId(entity.getTimeSlotId());
        entity.setReservationDate(entity.getReservationDate());
        entity.setStatus(entity.getStatus());
        entity.setCreatedAt(entity.getCreatedAt());
        entity.setUpdatedAt(entity.getUpdatedAt());
        entity.setDeletedAt(entity.getDeletedAt());

        return entity;
    }
}

package br.com.tauan.agendamento.timeslot.infrastructure.persistence.mapper;

import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.infrastructure.persistence.entity.TimeSlotJpaEntity;

public class TimeSlotPersistenceMapper {
    public static TimeSlot toDomain(TimeSlotJpaEntity entity) {
        return TimeSlot.restore(
                entity.getId(),
                entity.getCalendarId(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public static TimeSlotJpaEntity toJpaEntity(TimeSlot timeSlot) {
        TimeSlotJpaEntity entity = new TimeSlotJpaEntity();

        entity.setId(timeSlot.getId());
        entity.setCalendarId(timeSlot.getCalendarId());
        entity.setStartTime(timeSlot.getStartTime());
        entity.setEndTime(timeSlot.getEndTime());
        entity.setCreatedAt(timeSlot.getCreatedAt());
        entity.setUpdatedAt(timeSlot.getUpdatedAt());
        entity.setDeletedAt(timeSlot.getDeletedAt());

        return entity;
    }
}

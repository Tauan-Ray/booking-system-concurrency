package br.com.tauan.agendamento.calendar.infrastructure.persistence.mapper;

import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.infrastructure.persistence.entity.CalendarJpaEntity;

public class CalendarPersistenceMapper {
    public static Calendar toDomain(CalendarJpaEntity entity) {
        return Calendar.restore(
                entity.getId(),
                entity.getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public static CalendarJpaEntity toJpaEntity(Calendar calendar) {
        CalendarJpaEntity entity = new CalendarJpaEntity();

        entity.setId(calendar.getId());
        entity.setName(calendar.getName());
        entity.setCreatedAt(calendar.getCreatedAt());
        entity.setUpdatedAt(calendar.getUpdatedAt());
        entity.setDeletedAt(calendar.getDeletedAt());

        return entity;
    }
}

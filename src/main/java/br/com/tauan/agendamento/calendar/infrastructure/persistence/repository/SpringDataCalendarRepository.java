package br.com.tauan.agendamento.calendar.infrastructure.persistence.repository;

import br.com.tauan.agendamento.calendar.infrastructure.persistence.entity.CalendarJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataCalendarRepository
        extends JpaRepository<CalendarJpaEntity, UUID> {

    Optional<CalendarJpaEntity> findByName(String name);
}

package br.com.tauan.agendamento.calendar.domain.repository;

import br.com.tauan.agendamento.calendar.domain.entity.Calendar;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalendarRepository {
    List<Calendar> findAll();
    Optional<Calendar> findById(UUID id);
    Optional<Calendar> findByName(String name);
    Calendar save(Calendar calendar);
}

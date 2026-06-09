package br.com.tauan.agendamento.calendar.infrastructure.persistence;

import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.calendar.infrastructure.persistence.entity.CalendarJpaEntity;
import br.com.tauan.agendamento.calendar.infrastructure.persistence.mapper.CalendarPersistenceMapper;
import br.com.tauan.agendamento.calendar.infrastructure.persistence.repository.SpringDataCalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CalendarRepositoryImp implements CalendarRepository {

    private final SpringDataCalendarRepository repository;

    @Override
    public List<Calendar> findAll() {
        return repository.findAll()
                .stream()
                .map(CalendarPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Calendar> findById(UUID id) {
        return repository.findById(id)
                .map(CalendarPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Calendar> findByName(String name) {
        return repository.findByName(name)
                .map(CalendarPersistenceMapper::toDomain);
    }

    @Override
    public Calendar save(Calendar calendar) {
        CalendarJpaEntity saved = repository.save(
                CalendarPersistenceMapper.toJpaEntity(calendar)
        );

        return CalendarPersistenceMapper.toDomain(saved);
    }
}

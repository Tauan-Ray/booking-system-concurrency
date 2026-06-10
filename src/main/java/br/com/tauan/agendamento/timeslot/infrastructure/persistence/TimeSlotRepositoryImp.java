package br.com.tauan.agendamento.timeslot.infrastructure.persistence;

import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import br.com.tauan.agendamento.timeslot.infrastructure.persistence.entity.TimeSlotJpaEntity;
import br.com.tauan.agendamento.timeslot.infrastructure.persistence.mapper.TimeSlotPersistenceMapper;
import br.com.tauan.agendamento.timeslot.infrastructure.persistence.repository.SpringDataTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TimeSlotRepositoryImp implements TimeSlotRepository {

    private final SpringDataTimeSlotRepository repository;

    @Override
    public List<TimeSlot> findAll() {
        return repository.findAllByDeletedAtIsNull()
                .stream()
                .map(TimeSlotPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TimeSlot> findById(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .map(TimeSlotPersistenceMapper::toDomain);
    }

    @Override
    public Optional<TimeSlot> findByIdIncludingDeleted(UUID id) {
        return repository.findById(id)
                .map(TimeSlotPersistenceMapper::toDomain);
    }

    @Override
    public List<TimeSlot> findByCalendarId(UUID calendarId) {
        return repository.findByCalendarIdAndDeletedAtIsNull(calendarId)
                .stream()
                .map(TimeSlotPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsOverlappingTimeSlot(UUID calendarId, LocalDateTime startTime, LocalDateTime endTime) {
        return repository.existsOverlappingTimeSlot(
                calendarId,
                startTime,
                endTime
        );
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        TimeSlotJpaEntity saved = repository.save(
                TimeSlotPersistenceMapper.toJpaEntity(timeSlot)
        );

        return TimeSlotPersistenceMapper.toDomain(saved);
    }
}

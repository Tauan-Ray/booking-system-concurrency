package br.com.tauan.agendamento.reservation.infrastructure.persistence;

import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.reservation.infrastructure.persistence.entity.ReservationJpaEntity;
import br.com.tauan.agendamento.reservation.infrastructure.persistence.mapper.ReservationPersistenceMapper;
import br.com.tauan.agendamento.reservation.infrastructure.persistence.repository.SpringDataReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImp implements ReservationRepository {

    private final SpringDataReservationRepository repository;

    @Override
    public List<Reservation> findAll() {
        return repository.findAll()
                .stream()
                .map(ReservationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return repository.findById(id)
                .map(ReservationPersistenceMapper::toDomain);
    }

    @Override
    public List<Reservation> findByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(ReservationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findByTimeSlotId(UUID timeSlotId) {
        return repository.findByTimeSlotId(timeSlotId)
                .stream()
                .map(ReservationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationJpaEntity saved = repository.save(
                ReservationPersistenceMapper.toJpaEntity(reservation)
        );

        return ReservationPersistenceMapper.toDomain(saved);
    }

    @Override
    public boolean existsConfirmedReservation(UUID timeSlotId, LocalDate reservationDate) {
        return false;
    }
}

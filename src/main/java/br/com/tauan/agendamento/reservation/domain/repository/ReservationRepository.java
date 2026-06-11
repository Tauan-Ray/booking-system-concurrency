package br.com.tauan.agendamento.reservation.domain.repository;

import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    List<Reservation> findAll();
    Optional<Reservation> findById(UUID id);
    List<Reservation> findByUserId();
    List<Reservation> findByTimeSlotId(UUID timeSlotId);
    Reservation save(Reservation reservation);
    boolean existsConfirmedReservation(
            UUID timeSlotId,
            LocalDate reservationDate
    );
}
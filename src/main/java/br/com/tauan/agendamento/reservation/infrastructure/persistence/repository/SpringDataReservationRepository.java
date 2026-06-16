package br.com.tauan.agendamento.reservation.infrastructure.persistence.repository;

import br.com.tauan.agendamento.reservation.infrastructure.persistence.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataReservationRepository
        extends JpaRepository<ReservationJpaEntity, UUID> {

    List<ReservationJpaEntity> findByUserId(UUID userId);

    List<ReservationJpaEntity> findByTimeSlotId(UUID timeSlotId);

    @Query("""
        SELECT COUNT(r) > 0
        FROM ReservationJpaEntity r
        WHERE r.timeSlotId = :timeSlotId
          AND r.reservationDate = :reservationDate
          AND r.status = 'CONFIRMED'
    """)
    boolean existsConfirmedReservation(
            @Param("timeSlotId") UUID timeSlotId,
            @Param("reservationDate") LocalDate reservationDate
    );
}

package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.UUID;

public class ReservationTestBuilder {

    private UUID userId = UUID.randomUUID();
    private UUID timeSlotId = UUID.randomUUID();
    private LocalDate reservationDate = LocalDate.now();

    public static ReservationTestBuilder builder() {
        return new ReservationTestBuilder();
    }

    public ReservationTestBuilder withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public ReservationTestBuilder withTimeSlotId(UUID timeSlotId) {
        this.timeSlotId = timeSlotId;
        return this;
    }

    public ReservationTestBuilder withReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
        return this;
    }

    public Reservation build() {
        return Reservation.create(
                userId,
                timeSlotId,
                reservationDate
        );
    }

    public Reservation buildAndSave(ReservationRepository repository) {
        return repository.save(build());
    }
}

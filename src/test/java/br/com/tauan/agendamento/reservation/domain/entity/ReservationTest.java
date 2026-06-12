package br.com.tauan.agendamento.reservation.domain.entity;

import br.com.tauan.agendamento.reservation.domain.enums.ReservationStatus;
import br.com.tauan.agendamento.reservation.domain.exception.InvalidReservationException;
import br.com.tauan.agendamento.reservation.domain.exception.ReservationAlreadyCancelledException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldCreateReservationSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID timeSlotId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now();

        Reservation reservation = Reservation.create(userId, timeSlotId, reservationDate);

        assertEquals(userId, reservation.getUserId());
        assertEquals(timeSlotId, reservation.getTimeSlotId());
        assertEquals(reservationDate, reservation.getReservationDate());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        UUID timeSlotId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now();

        InvalidReservationException exception =
                assertThrows(
                        InvalidReservationException.class,
                        () -> Reservation.create(null, timeSlotId, reservationDate)
                );

        assertEquals(
                "User id cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotIdIsNull() {
        UUID userId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now();

        InvalidReservationException exception =
                assertThrows(
                        InvalidReservationException.class,
                        () -> Reservation.create(userId, null, reservationDate)
                );

        assertEquals(
                "Time slot id cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationDateIsNull() {
        UUID userId = UUID.randomUUID();
        UUID timeSlotId = UUID.randomUUID();

        InvalidReservationException exception =
                assertThrows(
                        InvalidReservationException.class,
                        () -> Reservation.create(userId, timeSlotId, null)
                );

        assertEquals(
                "Reservation date cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationDateIsInThePast() {
        UUID userId = UUID.randomUUID();
        UUID timeSlotId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now().minusDays(1);

        InvalidReservationException exception =
                assertThrows(
                        InvalidReservationException.class,
                        () -> Reservation.create(userId, timeSlotId, reservationDate)
                );

        assertEquals(
                "Reservation date must be today or later",
                exception.getMessage()
        );
    }

    @Test
    void shouldCancelReservation() {
        Reservation reservation = Reservation.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now()
        );

        reservation.cancel();

        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenReservationIsAlreadyCancelled() {
        Reservation reservation = Reservation.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now()
        );

        reservation.cancel();

        ReservationAlreadyCancelledException exception =
                assertThrows(
                        ReservationAlreadyCancelledException.class,
                        reservation::cancel
                );

        assertEquals(
                "Reservation already cancelled",
                exception.getMessage()
        );
    }
}

package br.com.tauan.agendamento.reservation.domain.entity;

import br.com.tauan.agendamento.reservation.domain.enums.ReservationStatus;
import br.com.tauan.agendamento.reservation.domain.exception.InvalidReservationException;
import br.com.tauan.agendamento.reservation.domain.exception.ReservationAlreadyCancelledException;
import br.com.tauan.agendamento.shared.domain.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation extends BaseEntity {
    private UUID userId;
    private UUID timeSlotId;
    private LocalDate reservationDate;
    private ReservationStatus status;

    private Reservation(
            UUID id,
            UUID userId,
            UUID timeSlotId,
            LocalDate reservationDate,
            ReservationStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        super(
                id,
                createdAt,
                updatedAt,
                deletedAt
        );

        this.userId = userId;
        this.timeSlotId = timeSlotId;
        this.status = status;
        this.reservationDate = reservationDate;
    }

    public static Reservation create(UUID userId, UUID timeSlotId, LocalDate reservationDate) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        validate(userId, timeSlotId, reservationDate);

        if (reservationDate.isBefore(today)) {
            throw new InvalidReservationException("Reservation date must be today or later");
        }

        return new Reservation(
                UUID.randomUUID(),
                userId,
                timeSlotId,
                reservationDate,
                ReservationStatus.CONFIRMED,
                now,
                now,
                null
        );
    }

    public static Reservation restore(
            UUID id,
            UUID userId,
            UUID timeSlotId,
            LocalDate reservationDate,
            ReservationStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        validate(userId, timeSlotId, reservationDate);

        return new Reservation(
                id,
                userId,
                timeSlotId,
                reservationDate,
                status,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    private static void validate(UUID userId, UUID timeSlotId, LocalDate reservationDate) {
        if (userId == null) {
            throw new InvalidReservationException("User id cannot be empty");
        }

        if (timeSlotId == null) {
            throw new InvalidReservationException("Time slot id cannot be empty");
        }

        if (reservationDate == null) {
            throw new InvalidReservationException("Reservation date cannot be empty");
        }
    }

    public void cancel() {
        if (status == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException();
        }

        this.status = ReservationStatus.CANCELLED;
        touch();
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public UUID getTimeSlotId() {
        return timeSlotId;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}

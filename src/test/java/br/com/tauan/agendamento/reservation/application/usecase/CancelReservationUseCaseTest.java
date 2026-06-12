package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.exception.ReservationNotFoundException;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.enums.ReservationStatus;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import br.com.tauan.agendamento.test.factory.ReservationTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelReservationUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AuthenticatedUserProvider auth;

    @InjectMocks
    private CancelReservationUseCase useCase;

    @Test
    void shouldCancelReservationWhenRequesterIsOwner() {
        Reservation reservation = ReservationTestBuilder.builder().build();

        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));

        when(auth.getUserId())
                .thenReturn(reservation.getUserId());

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        useCase.execute(reservation.getId());

        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus()
        );

        verify(reservationRepository)
                .findById(reservation.getId());

        verify(reservationRepository)
                .save(reservation);
    }

    @Test
    void shouldCancelReservationWhenRequesterIsAdmin() {
        Reservation reservation = ReservationTestBuilder.builder().build();
        UUID adminId = UUID.randomUUID();

        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));

        when(auth.getUserId())
                .thenReturn(adminId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(true);

        useCase.execute(reservation.getId());

        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus()
        );

        verify(reservationRepository)
                .save(reservation);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenRequesterIsNotOwnerNorAdmin() {
        Reservation reservation = ReservationTestBuilder.builder().build();
        UUID otherUserId = UUID.randomUUID();

        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));

        when(auth.getUserId())
                .thenReturn(otherUserId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        ForbiddenException exception =
                assertThrows(
                        ForbiddenException.class,
                        () -> useCase.execute(reservation.getId())
                );

        assertEquals(
                "You do not have permission to perform this action",
                exception.getMessage()
        );

        assertEquals(
                ReservationStatus.CONFIRMED,
                reservation.getStatus()
        );

        verify(reservationRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenReservationNotFound() {
        UUID reservationId = UUID.randomUUID();

        when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.empty());

        ReservationNotFoundException exception =
                assertThrows(
                        ReservationNotFoundException.class,
                        () -> useCase.execute(reservationId)
                );

        assertEquals(
                "Reservation not found",
                exception.getMessage()
        );

        verifyNoInteractions(auth);

        verify(reservationRepository, never())
                .save(any());
    }
}

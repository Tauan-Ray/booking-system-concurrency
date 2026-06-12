package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.exception.ReservationNotFoundException;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetReservationByIdUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AuthenticatedUserProvider auth;

    @InjectMocks
    private GetReservationByIdUseCase useCase;

    @Test
    void shouldReturnReservationWhenRequesterIsOwner() {
        Reservation reservation = ReservationTestBuilder.builder().build();

        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));

        when(auth.getUserId())
                .thenReturn(reservation.getUserId());

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        ReservationOutput output = useCase.execute(reservation.getId());

        assertNotNull(output);
        assertEquals(reservation.getId(), output.id());
        assertEquals(reservation.getUserId(), output.userId());

        verify(reservationRepository)
                .findById(reservation.getId());
    }

    @Test
    void shouldReturnReservationWhenRequesterIsAdmin() {
        Reservation reservation = ReservationTestBuilder.builder().build();
        UUID adminId = UUID.randomUUID();

        when(reservationRepository.findById(reservation.getId()))
                .thenReturn(Optional.of(reservation));

        when(auth.getUserId())
                .thenReturn(adminId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(true);

        ReservationOutput output = useCase.execute(reservation.getId());

        assertEquals(reservation.getId(), output.id());

        verify(reservationRepository)
                .findById(reservation.getId());
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

        verify(reservationRepository)
                .findById(reservationId);
    }
}

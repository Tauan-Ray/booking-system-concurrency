package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListReservationsByUserUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AuthenticatedUserProvider auth;

    @InjectMocks
    private ListReservationsByUserUseCase useCase;

    @Test
    void shouldReturnReservationsWhenRequesterIsOwner() {
        UUID userId = UUID.randomUUID();

        Reservation firstReservation =
                ReservationTestBuilder.builder().withUserId(userId).build();
        Reservation secondReservation =
                ReservationTestBuilder.builder().withUserId(userId).build();

        when(auth.getUserId())
                .thenReturn(userId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        when(reservationRepository.findByUserId(userId))
                .thenReturn(List.of(firstReservation, secondReservation));

        List<ReservationOutput> output = useCase.execute(userId);

        assertNotNull(output);
        assertEquals(2, output.size());

        verify(reservationRepository)
                .findByUserId(userId);
    }

    @Test
    void shouldReturnReservationsWhenRequesterIsAdmin() {
        UUID targetUserId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        Reservation reservation =
                ReservationTestBuilder.builder().withUserId(targetUserId).build();

        when(auth.getUserId())
                .thenReturn(adminId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(true);

        when(reservationRepository.findByUserId(targetUserId))
                .thenReturn(List.of(reservation));

        List<ReservationOutput> output = useCase.execute(targetUserId);

        assertEquals(1, output.size());

        verify(reservationRepository)
                .findByUserId(targetUserId);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenRequesterIsNotOwnerNorAdmin() {
        UUID targetUserId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        when(auth.getUserId())
                .thenReturn(otherUserId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        ForbiddenException exception =
                assertThrows(
                        ForbiddenException.class,
                        () -> useCase.execute(targetUserId)
                );

        assertEquals(
                "You do not have permission to perform this action",
                exception.getMessage()
        );

        verify(reservationRepository, never())
                .findByUserId(any());
    }
}

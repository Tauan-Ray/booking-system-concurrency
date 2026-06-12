package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.CreateReservationInput;
import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.exception.ReservationConflictException;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import br.com.tauan.agendamento.user.application.exception.UserNotFoundException;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateReservationUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUserProvider auth;

    @InjectMocks
    private CreateReservationUseCase useCase;

    private TimeSlot timeSlot() {
        return TimeSlot.create(
                UUID.randomUUID(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        UUID timeSlotId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now().plusDays(1);

        CreateReservationInput input =
                new CreateReservationInput(null, timeSlotId, reservationDate);

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.of(timeSlot()));

        when(reservationRepository.existsConfirmedReservation(timeSlotId, reservationDate))
                .thenReturn(false);

        when(auth.getUserId())
                .thenReturn(userId);

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationOutput output = useCase.execute(input);

        assertNotNull(output.id());
        assertEquals(userId, output.userId());
        assertEquals(timeSlotId, output.timeSlotId());
        assertEquals(reservationDate, output.reservationDate());

        verify(timeSlotRepository)
                .findById(timeSlotId);

        verify(reservationRepository)
                .save(any(Reservation.class));

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldCreateReservationForAnotherUserWhenRequesterIsAdmin() {
        UUID timeSlotId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now().plusDays(1);

        CreateReservationInput input =
                new CreateReservationInput(targetUserId, timeSlotId, reservationDate);

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.of(timeSlot()));

        when(reservationRepository.existsConfirmedReservation(timeSlotId, reservationDate))
                .thenReturn(false);

        when(auth.hasRole("ADMIN"))
                .thenReturn(true);

        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.of(UserTestBuilder.builder().build()));

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationOutput output = useCase.execute(input);

        assertEquals(targetUserId, output.userId());

        verify(userRepository)
                .findById(targetUserId);

        verify(reservationRepository)
                .save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotNotFound() {
        UUID timeSlotId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now().plusDays(1);

        CreateReservationInput input =
                new CreateReservationInput(null, timeSlotId, reservationDate);

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.empty());

        TimeSlotNotFoundException exception =
                assertThrows(
                        TimeSlotNotFoundException.class,
                        () -> useCase.execute(input)
                );

        assertEquals(
                "Time slot not found",
                exception.getMessage()
        );

        verify(reservationRepository, never())
                .existsConfirmedReservation(any(), any());

        verify(reservationRepository, never())
                .save(any());

        verifyNoInteractions(userRepository, auth);
    }

    @Test
    void shouldThrowExceptionWhenReservationConflicts() {
        UUID timeSlotId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now().plusDays(1);

        CreateReservationInput input =
                new CreateReservationInput(null, timeSlotId, reservationDate);

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.of(timeSlot()));

        when(reservationRepository.existsConfirmedReservation(timeSlotId, reservationDate))
                .thenReturn(true);

        when(auth.hasRole("ADMIN"))
                .thenReturn(false);

        ReservationConflictException exception =
                assertThrows(
                        ReservationConflictException.class,
                        () -> useCase.execute(input)
                );

        assertEquals(
                "Time slot is already reserved for this date",
                exception.getMessage()
        );

        verify(reservationRepository, never())
                .save(any());

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenAdminProvidesNonExistentUser() {
        UUID timeSlotId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        LocalDate reservationDate = LocalDate.now().plusDays(1);

        CreateReservationInput input =
                new CreateReservationInput(targetUserId, timeSlotId, reservationDate);

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.of(timeSlot()));

        when(reservationRepository.existsConfirmedReservation(timeSlotId, reservationDate))
                .thenReturn(false);

        when(auth.hasRole("ADMIN"))
                .thenReturn(true);

        when(userRepository.findById(targetUserId))
                .thenReturn(Optional.empty());

        UserNotFoundException exception =
                assertThrows(
                        UserNotFoundException.class,
                        () -> useCase.execute(input)
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(reservationRepository, never())
                .save(any());
    }
}

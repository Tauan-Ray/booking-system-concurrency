package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.test.factory.ReservationTestBuilder;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListReservationsByTimeSlotUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private ListReservationsByTimeSlotUseCase useCase;

    @Test
    void shouldReturnReservationsWhenTimeSlotExists() {
        UUID timeSlotId = UUID.randomUUID();

        TimeSlot timeSlot = TimeSlot.create(
                UUID.randomUUID(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        Reservation firstReservation =
                ReservationTestBuilder.builder().withTimeSlotId(timeSlotId).build();
        Reservation secondReservation =
                ReservationTestBuilder.builder().withTimeSlotId(timeSlotId).build();

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.of(timeSlot));

        when(reservationRepository.findByTimeSlotId(timeSlotId))
                .thenReturn(List.of(firstReservation, secondReservation));

        List<ReservationOutput> output = useCase.execute(timeSlotId);

        assertNotNull(output);
        assertEquals(2, output.size());

        verify(timeSlotRepository)
                .findById(timeSlotId);

        verify(reservationRepository)
                .findByTimeSlotId(timeSlotId);
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotNotFound() {
        UUID timeSlotId = UUID.randomUUID();

        when(timeSlotRepository.findById(timeSlotId))
                .thenReturn(Optional.empty());

        TimeSlotNotFoundException exception =
                assertThrows(
                        TimeSlotNotFoundException.class,
                        () -> useCase.execute(timeSlotId)
                );

        assertEquals(
                "Time slot not found",
                exception.getMessage()
        );

        verify(timeSlotRepository)
                .findById(timeSlotId);

        verifyNoInteractions(reservationRepository);
    }
}

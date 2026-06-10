package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTimeSlotByIdUseCaseTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private GetTimeSlotByIdUseCase useCase;

    @Test
    void shouldReturnTimeSlotWhenFound() {
        TimeSlot timeSlot = TimeSlot.create(
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        when(timeSlotRepository.findById(timeSlot.getId()))
                .thenReturn(Optional.of(timeSlot));

        TimeSlotOutput output = useCase.execute(timeSlot.getId());

        assertNotNull(output);
        assertEquals(timeSlot.getId(), output.id());
        assertEquals(timeSlot.getCalendarId(), output.calendarId());

        verify(timeSlotRepository)
                .findById(timeSlot.getId());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotNotFound() {
        when(timeSlotRepository.findById(any()))
                .thenReturn(Optional.empty());

        TimeSlotNotFoundException exception =
                assertThrows(
                        TimeSlotNotFoundException.class,
                        () -> useCase.execute(UUID.randomUUID())
                );

        assertEquals(
                "Time slot not found",
                exception.getMessage()
        );

        verify(timeSlotRepository)
                .findById(any());
    }
}

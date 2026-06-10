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
class GetTimeSlotByIdIncludingDeletedUseCaseTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private GetTimeSlotByIdIncludingDeletedUseCase useCase;

    @Test
    void shouldReturnTimeSlotIncludingDeletedWhenFound() {
        TimeSlot timeSlot = TimeSlot.create(
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );
        timeSlot.archive();

        when(timeSlotRepository.findByIdIncludingDeleted(timeSlot.getId()))
                .thenReturn(Optional.of(timeSlot));

        TimeSlotOutput output = useCase.execute(timeSlot.getId());

        assertNotNull(output);
        assertEquals(timeSlot.getId(), output.id());
        assertEquals(timeSlot.getCalendarId(), output.calendarId());
        assertNotNull(output.deletedAt());

        verify(timeSlotRepository)
                .findByIdIncludingDeleted(timeSlot.getId());

        verify(timeSlotRepository, never())
                .findById(any());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotNotFound() {
        when(timeSlotRepository.findByIdIncludingDeleted(any()))
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
                .findByIdIncludingDeleted(any());

        verify(timeSlotRepository, never())
                .findById(any());
    }
}

package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchiveTimeSlotUseCaseTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private ArchiveTimeSlotUseCase useCase;

    @Test
    void shouldArchiveTimeSlot() {
        TimeSlot existingTimeSlot = TimeSlot.create(
                UUID.randomUUID(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        when(timeSlotRepository.findById(existingTimeSlot.getId()))
                .thenReturn(Optional.of(existingTimeSlot));

        useCase.execute(existingTimeSlot.getId());

        assertTrue(existingTimeSlot.isDeleted());

        verify(timeSlotRepository)
                .findById(existingTimeSlot.getId());
        verify(timeSlotRepository)
                .save(existingTimeSlot);
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotDoesNotExist() {
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
        verify(timeSlotRepository, never())
                .save(any());
    }
}

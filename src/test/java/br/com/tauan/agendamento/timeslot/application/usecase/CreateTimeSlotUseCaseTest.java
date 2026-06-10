package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.timeslot.application.dto.CreateTimeSlotInput;
import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotConflictException;
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
class CreateTimeSlotUseCaseTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private CreateTimeSlotUseCase useCase;

    @Test
    void shouldCreateTimeSlotSuccessfully() {
        UUID calendarId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        CreateTimeSlotInput input =
                new CreateTimeSlotInput(calendarId, startTime, endTime);

        when(calendarRepository.findById(calendarId))
                .thenReturn(Optional.of(Calendar.create("Sala Principal")));

        when(timeSlotRepository.existsOverlappingTimeSlot(
                calendarId, startTime, endTime
        )).thenReturn(false);

        when(timeSlotRepository.save(any(TimeSlot.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TimeSlotOutput output = useCase.execute(input);

        assertNotNull(output.id());
        assertEquals(calendarId, output.calendarId());
        assertEquals(startTime, output.startTime());
        assertEquals(endTime, output.endTime());

        verify(calendarRepository)
                .findById(calendarId);

        verify(timeSlotRepository)
                .existsOverlappingTimeSlot(calendarId, startTime, endTime);

        verify(timeSlotRepository)
                .save(any(TimeSlot.class));
    }

    @Test
    void shouldThrowExceptionWhenCalendarNotFound() {
        UUID calendarId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        CreateTimeSlotInput input =
                new CreateTimeSlotInput(calendarId, startTime, endTime);

        when(calendarRepository.findById(calendarId))
                .thenReturn(Optional.empty());

        CalendarNotFoundException exception =
                assertThrows(
                        CalendarNotFoundException.class,
                        () -> useCase.execute(input)
                );

        assertEquals(
                "Calendar not found",
                exception.getMessage()
        );

        verify(timeSlotRepository, never())
                .existsOverlappingTimeSlot(any(), any(), any());

        verify(timeSlotRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotConflicts() {
        UUID calendarId = UUID.randomUUID();
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        CreateTimeSlotInput input =
                new CreateTimeSlotInput(calendarId, startTime, endTime);

        when(calendarRepository.findById(calendarId))
                .thenReturn(Optional.of(Calendar.create("Sala Principal")));

        when(timeSlotRepository.existsOverlappingTimeSlot(
                calendarId, startTime, endTime
        )).thenReturn(true);

        TimeSlotConflictException exception =
                assertThrows(
                        TimeSlotConflictException.class,
                        () -> useCase.execute(input)
                );

        assertEquals(
                "Time slot overlaps with an existing time slot",
                exception.getMessage()
        );

        verify(timeSlotRepository, never())
                .save(any());
    }
}

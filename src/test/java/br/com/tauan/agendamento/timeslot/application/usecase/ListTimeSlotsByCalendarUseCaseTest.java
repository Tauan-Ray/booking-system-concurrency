package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListTimeSlotsByCalendarUseCaseTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private ListTimeSlotsByCalendarUseCase useCase;

    @Test
    void shouldReturnTimeSlotsWhenCalendarExists() {
        UUID calendarId = UUID.randomUUID();

        Calendar calendar = Calendar.create("Meeting Room");

        TimeSlot firstTimeSlot = TimeSlot.create(
                calendarId,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        TimeSlot secondTimeSlot = TimeSlot.create(
                calendarId,
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(3)
        );

        when(calendarRepository.findById(calendarId))
                .thenReturn(Optional.of(calendar));

        when(timeSlotRepository.findByCalendarId(calendarId))
                .thenReturn(List.of(firstTimeSlot, secondTimeSlot));

        List<TimeSlotOutput> output =
                useCase.execute(calendarId);

        assertNotNull(output);
        assertEquals(2, output.size());

        verify(calendarRepository)
                .findById(calendarId);

        verify(timeSlotRepository)
                .findByCalendarId(calendarId);
    }

    @Test
    void shouldThrowExceptionWhenCalendarNotFound() {
        UUID calendarId = UUID.randomUUID();

        when(calendarRepository.findById(calendarId))
                .thenReturn(Optional.empty());

        CalendarNotFoundException exception =
                assertThrows(
                        CalendarNotFoundException.class,
                        () -> useCase.execute(calendarId)
                );

        assertEquals(
                "Calendar not found",
                exception.getMessage()
        );

        verify(calendarRepository)
                .findById(calendarId);

        verifyNoInteractions(timeSlotRepository);
    }
}
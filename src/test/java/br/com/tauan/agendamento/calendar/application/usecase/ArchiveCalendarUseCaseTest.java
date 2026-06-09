package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
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
class ArchiveCalendarUseCaseTest {

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private ArchiveCalendarUseCase useCase;

    @Test
    void shouldArchiveCalendar() {
        Calendar existingCalendar = Calendar.create("Sala Pricipal");

        when(calendarRepository.findById(existingCalendar.getId()))
                .thenReturn(Optional.of(existingCalendar));

        useCase.execute(existingCalendar.getId());

        assertTrue(existingCalendar.isDeleted());

        verify(calendarRepository)
                .findById(existingCalendar.getId());
        verify(calendarRepository)
                .save(existingCalendar);
    }

    @Test
    void shouldThrowExceptionWhenCalendarDoesNotExist() {
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
        verify(calendarRepository, never())
                .save(any());
    }
}

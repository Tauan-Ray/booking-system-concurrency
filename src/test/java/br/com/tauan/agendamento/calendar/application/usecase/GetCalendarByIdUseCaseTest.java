package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCalendarByIdUseCaseTest {

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private GetCalendarByIdUseCase useCase;

    @Test
    void shouldReturnCalendarWhenFound() {
        Calendar calendar = Calendar.create("Sala Principal");

        when(calendarRepository.findById(calendar.getId()))
                .thenReturn(Optional.of(calendar));

        CalendarOutput output = useCase.execute(calendar.getId());

        assertNotNull(output);
        assertEquals(calendar.getId(), output.id());
        assertEquals("Sala Principal", output.name());

        verify(calendarRepository)
                .findById(calendar.getId());
    }

    @Test
    void shouldThrowExceptionWhenCalendarNotFound() {
        when(calendarRepository.findById(any()))
                .thenReturn(Optional.empty());

        CalendarNotFoundException exception =
                assertThrows(
                        CalendarNotFoundException.class,
                        () -> useCase.execute(java.util.UUID.randomUUID())
                );

        assertEquals(
                "Calendar not found",
                exception.getMessage()
        );

        verify(calendarRepository)
                .findById(any());
    }
}
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
class GetCalendarByIdIncludingDeletedUseCaseTest {

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private GetCalendarByIdIncludingDeletedUseCase useCase;

    @Test
    void shouldReturnCalendarIncludingDeletedWhenFound() {
        Calendar calendar = Calendar.create("Sala Principal");
        calendar.archive();

        when(calendarRepository.findByIdIncludingDeleted(calendar.getId()))
                .thenReturn(Optional.of(calendar));

        CalendarOutput output = useCase.execute(calendar.getId());

        assertNotNull(output);
        assertEquals(calendar.getId(), output.id());
        assertEquals("Sala Principal", output.name());
        assertNotNull(output.deletedAt());

        verify(calendarRepository)
                .findByIdIncludingDeleted(calendar.getId());

        verify(calendarRepository, never())
                .findById(any());
    }

    @Test
    void shouldThrowExceptionWhenCalendarNotFound() {
        when(calendarRepository.findByIdIncludingDeleted(any()))
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
                .findByIdIncludingDeleted(any());

        verify(calendarRepository, never())
                .findById(any());
    }
}
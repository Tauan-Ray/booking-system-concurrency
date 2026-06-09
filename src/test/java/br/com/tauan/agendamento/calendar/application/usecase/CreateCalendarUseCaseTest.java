package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.dto.CreateCalendarInput;
import br.com.tauan.agendamento.calendar.application.exception.CalendarAlreadyExistsException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCalendarUseCaseTest {

    @Mock
    private CalendarRepository calendarRepository;

    @InjectMocks
    private CreateCalendarUseCase useCase;

    @Test
    void shouldCreateCalendarSuccessfully() {
        CreateCalendarInput input = new CreateCalendarInput("Sala Principal");

        when(calendarRepository.findByName("Sala Principal"))
                .thenReturn(Optional.empty());

        when(calendarRepository.save(any(Calendar.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CalendarOutput output = useCase.execute(input);

        assertEquals("Sala Principal", output.name());
        assertNotNull(output.id());

        verify(calendarRepository)
                .save(any(Calendar.class));

        verify(calendarRepository)
                .findByName("Sala Principal");
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        Calendar existingCalendar =
                Calendar.create("Sala Principal");

        CreateCalendarInput input = new CreateCalendarInput("Sala Principal");

        when(calendarRepository.findByName(any()))
                .thenReturn(Optional.of(existingCalendar));

        CalendarAlreadyExistsException exception =
                assertThrows(
                        CalendarAlreadyExistsException.class,
                        () -> useCase.execute(input)
                );

        verify(calendarRepository, never())
                .save(any());

        assertEquals(
                "Calendar already exists",
                exception.getMessage()
        );
    }
}
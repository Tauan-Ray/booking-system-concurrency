package br.com.tauan.agendamento.calendar.domain.entity;

import br.com.tauan.agendamento.calendar.domain.exception.CalendarAlreadyDeletedException;
import br.com.tauan.agendamento.calendar.domain.exception.InvalidCalendarException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalendarTest {

    @Test
    void shouldCreateCalendarSuccessfully() {
        Calendar calendar = Calendar.create("Sala Principal");

        assertEquals("Sala Principal", calendar.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidCalendarException exception =
                assertThrows(
                        InvalidCalendarException.class,
                        () -> Calendar.create(null)
                );

        assertEquals(
                "Name cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidCalendarException exception =
                assertThrows(
                        InvalidCalendarException.class,
                        () -> Calendar.create("")
                );

        assertEquals(
                "Name cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldArchiveCalendar() {
        Calendar calendar = Calendar.create("Sala Principal");

        calendar.archive();

        assertTrue(calendar.isDeleted());
        assertNotNull(calendar.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenCalendarIsAlreadyDeleted() {
        Calendar calendar = Calendar.create("Sala Principal");

        calendar.archive();

        CalendarAlreadyDeletedException exception =
                assertThrows(
                        CalendarAlreadyDeletedException.class,
                        calendar::archive
                );

        assertEquals(
                "Calendar already deleted",
                exception.getMessage()
        );
    }
}

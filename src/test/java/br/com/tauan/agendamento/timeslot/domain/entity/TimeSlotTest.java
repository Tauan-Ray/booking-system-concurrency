package br.com.tauan.agendamento.timeslot.domain.entity;

import br.com.tauan.agendamento.timeslot.domain.exception.InvalidTimeSlotException;
import br.com.tauan.agendamento.timeslot.domain.exception.TimeSlotAlreadyDeletedException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    @Test
    void shouldCreateTimeSlotSuccessfully() {
        UUID calendarId = UUID.randomUUID();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = startTime.plusHours(1);

        TimeSlot timeSlot = TimeSlot.create(calendarId, startTime, endTime);

        assertEquals(calendarId, timeSlot.getCalendarId());
        assertEquals(startTime, timeSlot.getStartTime());
        assertEquals(endTime, timeSlot.getEndTime());
    }

    @Test
    void shouldThrowExceptionWhenCalendarIdIsNull() {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = startTime.plusHours(1);

        InvalidTimeSlotException exception =
                assertThrows(
                        InvalidTimeSlotException.class,
                        () -> TimeSlot.create(null, startTime, endTime)
                );

        assertEquals(
                "Calendar id cannot be empty",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStartTimeIsNull() {
        UUID calendarId = UUID.randomUUID();
        LocalTime endTime = LocalTime.of(10, 0);

        InvalidTimeSlotException exception =
                assertThrows(
                        InvalidTimeSlotException.class,
                        () -> TimeSlot.create(calendarId, null, endTime)
                );

        assertEquals(
                "Start time cannot be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenEndTimeIsNull() {
        UUID calendarId = UUID.randomUUID();
        LocalTime startTime = LocalTime.of(9, 0);

        InvalidTimeSlotException exception =
                assertThrows(
                        InvalidTimeSlotException.class,
                        () -> TimeSlot.create(calendarId, startTime, null)
                );

        assertEquals(
                "End time cannot be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenStartTimeIsNotBeforeEndTime() {
        UUID calendarId = UUID.randomUUID();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = startTime.minusHours(1);

        InvalidTimeSlotException exception =
                assertThrows(
                        InvalidTimeSlotException.class,
                        () -> TimeSlot.create(calendarId, startTime, endTime)
                );

        assertEquals(
                "Start time must be before end time",
                exception.getMessage()
        );
    }

    @Test
    void shouldArchiveTimeSlot() {
        TimeSlot timeSlot = TimeSlot.create(
                UUID.randomUUID(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        timeSlot.archive();

        assertTrue(timeSlot.isDeleted());
        assertNotNull(timeSlot.getDeletedAt());
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotIsAlreadyDeleted() {
        TimeSlot timeSlot = TimeSlot.create(
                UUID.randomUUID(),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0)
        );

        timeSlot.archive();

        TimeSlotAlreadyDeletedException exception =
                assertThrows(
                        TimeSlotAlreadyDeletedException.class,
                        timeSlot::archive
                );

        assertEquals(
                "Time slot already deleted",
                exception.getMessage()
        );
    }
}

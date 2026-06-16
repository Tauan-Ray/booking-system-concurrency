package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;

import java.time.LocalTime;
import java.util.UUID;

public class TimeSlotTestBuilder {

    private UUID calendarId = UUID.randomUUID();
    private LocalTime startTime = LocalTime.of(9, 0);
    private LocalTime endTime = LocalTime.of(10, 0);

    public static TimeSlotTestBuilder builder() {
        return new TimeSlotTestBuilder();
    }

    public TimeSlotTestBuilder withCalendarId(UUID calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    public TimeSlotTestBuilder withStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public TimeSlotTestBuilder withEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public TimeSlot build() {
        return TimeSlot.create(
                calendarId,
                startTime,
                endTime
        );
    }

    public TimeSlot buildAndSave(TimeSlotRepository repository) {
        return repository.save(build());
    }
}
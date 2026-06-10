package br.com.tauan.agendamento.timeslot.application.mapper;

import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;

public class TimeSlotMapper {
    public static TimeSlotOutput toOutput(TimeSlot timeSlot) {
        return new TimeSlotOutput(
                timeSlot.getId(),
                timeSlot.getCalendarId(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getCreatedAt(),
                timeSlot.getUpdatedAt(),
                timeSlot.getDeletedAt()
        );
    }
}

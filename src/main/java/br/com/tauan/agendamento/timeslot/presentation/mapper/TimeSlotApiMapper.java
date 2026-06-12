package br.com.tauan.agendamento.timeslot.presentation.mapper;

import br.com.tauan.agendamento.timeslot.application.dto.CreateTimeSlotInput;
import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.presentation.dto.request.CreateTimeSlotRequest;
import br.com.tauan.agendamento.timeslot.presentation.dto.response.TimeSlotResponse;

import java.util.List;

public class TimeSlotApiMapper {
    public static TimeSlotResponse toResponse(TimeSlotOutput output) {
        return new TimeSlotResponse(
                output.id(),
                output.calendarId(),
                output.startTime(),
                output.endTime(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    public static List<TimeSlotResponse> toResponseList(List<TimeSlotOutput> outputs) {
        return outputs.stream()
                .map(TimeSlotApiMapper::toResponse)
                .toList();
    }

    public static CreateTimeSlotInput toInput(CreateTimeSlotRequest request) {
        return new CreateTimeSlotInput(
                request.calendarId(),
                request.startTime(),
                request.endTime()
        );
    }
}

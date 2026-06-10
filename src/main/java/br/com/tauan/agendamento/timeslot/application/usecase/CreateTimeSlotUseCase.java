package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.timeslot.application.dto.CreateTimeSlotInput;
import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotConflictException;
import br.com.tauan.agendamento.timeslot.application.mapper.TimeSlotMapper;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTimeSlotUseCase {

    private final TimeSlotRepository timeSlotRepository;
    private final CalendarRepository calendarRepository;

    public TimeSlotOutput execute(CreateTimeSlotInput input) {
        calendarRepository.findById(input.calendarId())
                .orElseThrow(CalendarNotFoundException::new);



        boolean hasConflict = timeSlotRepository.existsOverlappingTimeSlot(
                input.calendarId(),
                input.startTime(),
                input.endTime()
        );

        if (hasConflict) {
            throw new TimeSlotConflictException();
        }

        TimeSlot timeSlot = TimeSlot.create(
                input.calendarId(),
                input.startTime(),
                input.endTime()
        );

        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlot);

        return TimeSlotMapper.toOutput(savedTimeSlot);
    }
}

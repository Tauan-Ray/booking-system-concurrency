package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.mapper.TimeSlotMapper;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListTimeSlotsByCalendarUseCase {

    private final TimeSlotRepository timeSlotRepository;
    private final CalendarRepository calendarRepository;

    public List<TimeSlotOutput> execute(UUID calendarId) {
        calendarRepository.findById(calendarId)
                .orElseThrow(CalendarNotFoundException::new);

        return timeSlotRepository.findByCalendarId(calendarId)
                .stream()
                .map(TimeSlotMapper::toOutput)
                .toList();
    }

}

package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.mapper.CalendarMapper;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListCalendarsUseCase {

    private final CalendarRepository calendarRepository;

    public List<CalendarOutput> execute() {
        List<Calendar> calendars = calendarRepository.findAll();

        return calendars.stream()
                .map(CalendarMapper::toOutput)
                .toList();

    }
}

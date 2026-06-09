package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchiveCalendarUseCase {

    private final CalendarRepository calendarRepository;

    public void execute(UUID id) {
        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(CalendarNotFoundException::new);

        calendar.archive();

        calendarRepository.save(calendar);
    }
}

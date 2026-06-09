package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.application.mapper.CalendarMapper;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCalendarByIdIncludingDeletedUseCase {

    private final CalendarRepository calendarRepository;

    public CalendarOutput execute(UUID id) {
        Calendar calendar = calendarRepository.findByIdIncludingDeleted(id)
                .orElseThrow(CalendarNotFoundException::new);

        return CalendarMapper.toOutput(calendar);
    }
}

package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.application.mapper.CalendarMapper;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCalendarByIdUseCase {

    private final CalendarRepository calendarRepository;
    private final AuthenticatedUserProvider auth;

    public CalendarOutput execute(UUID id) {
        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(CalendarNotFoundException::new);

        return CalendarMapper.toOutput(calendar);
    }
}

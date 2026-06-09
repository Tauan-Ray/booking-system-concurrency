package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.dto.CalendarOutput;
import br.com.tauan.agendamento.calendar.application.dto.CreateCalendarInput;
import br.com.tauan.agendamento.calendar.application.exception.CalendarAlreadyExistsException;
import br.com.tauan.agendamento.calendar.application.mapper.CalendarMapper;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCalendarUseCase {

    private final CalendarRepository calendarRepository;
    private final AuthenticatedUserProvider auth;

    public CalendarOutput execute(CreateCalendarInput input) {
        if (!auth.hasRole("ADMIN")) {
            throw new ForbiddenException();
        }

        calendarRepository.findByName(input.name())
                .ifPresent(calendar -> {
                    throw new CalendarAlreadyExistsException();
                });

        Calendar calendar = Calendar.create(input.name());

        Calendar savedCalendar = calendarRepository.save(calendar);

        return CalendarMapper.toOutput(savedCalendar);
    }
}

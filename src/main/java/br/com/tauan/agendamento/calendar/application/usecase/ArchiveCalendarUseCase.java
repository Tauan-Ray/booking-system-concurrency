package br.com.tauan.agendamento.calendar.application.usecase;

import br.com.tauan.agendamento.calendar.application.exception.CalendarNotFoundException;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchiveCalendarUseCase {

    private final CalendarRepository calendarRepository;
    private final AuthenticatedUserProvider auth;

    public void execute(UUID id) {
        if (!auth.hasRole("ADMIN")) {
            throw new ForbiddenException();
        }

        Calendar calendar = calendarRepository.findById(id)
                .orElseThrow(CalendarNotFoundException::new);

        calendar.archive();

        calendarRepository.save(calendar);
    }
}

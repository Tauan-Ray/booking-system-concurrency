package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchiveTimeSlotUseCase {

    private TimeSlotRepository timeSlotRepository;

    public void execute(UUID id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(TimeSlotNotFoundException::new);

        timeSlot.archive();

        timeSlotRepository.save(timeSlot);
    }
}

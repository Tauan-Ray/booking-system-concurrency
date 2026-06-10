package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.application.mapper.TimeSlotMapper;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTimeSlotByIdUseCase {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotOutput execute(UUID id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(TimeSlotNotFoundException::new);

        return TimeSlotMapper.toOutput(timeSlot);
    }
}

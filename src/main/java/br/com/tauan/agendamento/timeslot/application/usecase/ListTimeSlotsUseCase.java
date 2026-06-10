package br.com.tauan.agendamento.timeslot.application.usecase;

import br.com.tauan.agendamento.timeslot.application.dto.TimeSlotOutput;
import br.com.tauan.agendamento.timeslot.application.mapper.TimeSlotMapper;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTimeSlotsUseCase {

    private final TimeSlotRepository timeSlotRepository;

    public List<TimeSlotOutput> execute() {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();

        return timeSlots.stream()
                .map(TimeSlotMapper::toOutput)
                .toList();

    }
}

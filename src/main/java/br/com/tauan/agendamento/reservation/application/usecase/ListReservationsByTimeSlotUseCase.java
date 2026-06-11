package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.mapper.ReservationMapper;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListReservationsByTimeSlotUseCase {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    public List<ReservationOutput> execute(UUID timeSlotId) {
        timeSlotRepository.findById(timeSlotId)
                .orElseThrow(TimeSlotNotFoundException::new);

        return reservationRepository.findByTimeSlotId(timeSlotId)
                .stream()
                .map(ReservationMapper::toOutput)
                .toList();
    }
}

package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.CreateReservationInput;
import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.exception.ReservationConflictException;
import br.com.tauan.agendamento.reservation.application.mapper.ReservationMapper;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.timeslot.application.exception.TimeSlotNotFoundException;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import br.com.tauan.agendamento.user.application.exception.UserNotFoundException;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateReservationUseCase {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserProvider auth;

    public ReservationOutput execute(CreateReservationInput input) {
        timeSlotRepository.findById(input.timeSlotId())
                .orElseThrow(TimeSlotNotFoundException::new);

        boolean existsConfirmedReservation = reservationRepository.existsConfirmedReservation(
                input.timeSlotId(),
                input.reservationDate()
        );

        UUID finalUserId = auth.getUserId();
        if (auth.hasRole("ADMIN") && input.userId() != null) {
            userRepository.findById(input.userId())
                    .orElseThrow(UserNotFoundException::new);

            finalUserId = input.userId();
        }

        if (existsConfirmedReservation) {
            throw new ReservationConflictException();
        }

        Reservation reservation = Reservation.create(
                finalUserId,
                input.timeSlotId(),
                input.reservationDate()
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationMapper.toOutput(savedReservation);

    }
}

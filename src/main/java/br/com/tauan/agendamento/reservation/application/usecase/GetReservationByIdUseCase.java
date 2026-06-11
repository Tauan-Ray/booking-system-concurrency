package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.exception.ReservationNotFoundException;
import br.com.tauan.agendamento.reservation.application.mapper.ReservationMapper;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetReservationByIdUseCase {

    private final ReservationRepository reservationRepository;
    private final AuthenticatedUserProvider auth;

    public ReservationOutput execute(UUID id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);

        UUID requesterId = auth.getUserId();
        boolean isOwner = reservation.getUserId().equals(requesterId);
        boolean isAdmin = auth.hasRole("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException();
        }

        return ReservationMapper.toOutput(reservation);
    }
}

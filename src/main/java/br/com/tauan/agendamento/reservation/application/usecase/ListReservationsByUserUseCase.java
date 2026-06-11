package br.com.tauan.agendamento.reservation.application.usecase;

import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.mapper.ReservationMapper;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.application.contract.AuthenticatedUserProvider;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListReservationsByUserUseCase {

    private final ReservationRepository reservationRepository;
    private final AuthenticatedUserProvider auth;

    public List<ReservationOutput> execute(UUID id) {
        UUID requesterId = auth.getUserId();

        boolean isRequestedUser = id.equals(requesterId);
        boolean isAdmin = auth.hasRole("ADMIN");

        if (!isRequestedUser && !isAdmin) {
            throw new ForbiddenException();
        }
        List<Reservation> reservations = reservationRepository.findByUserId(id);

        return reservations.stream()
                .map(ReservationMapper::toOutput)
                .toList();
    }
}

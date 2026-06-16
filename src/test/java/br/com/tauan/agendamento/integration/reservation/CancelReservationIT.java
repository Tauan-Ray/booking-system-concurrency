package br.com.tauan.agendamento.integration.reservation;

import br.com.tauan.agendamento.IntegrationTest;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.reservation.application.dto.CreateReservationInput;
import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.usecase.CancelReservationUseCase;
import br.com.tauan.agendamento.reservation.application.usecase.CreateReservationUseCase;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.enums.ReservationStatus;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.shared.domain.exception.ForbiddenException;
import br.com.tauan.agendamento.test.factory.CalendarTestBuilder;
import br.com.tauan.agendamento.test.factory.ReservationTestBuilder;
import br.com.tauan.agendamento.test.factory.TimeSlotTestBuilder;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.enums.UserRole;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CancelReservationIT extends IntegrationTest {

    @Autowired
    private CancelReservationUseCase useCase;

    @Autowired
    private CreateReservationUseCase createReservationUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void shouldCancelReservationSuccessfully() {
        User user = UserTestBuilder.builder().buildAndSave(userRepository);
        Calendar calendar = CalendarTestBuilder.builder().buildAndSave(calendarRepository);
        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);
        Reservation reservation = ReservationTestBuilder.builder()
                .withUserId(user.getId())
                .withTimeSlotId(timeSlot.getId())
                .buildAndSave(reservationRepository);

        authenticateAs(user.getId(), "USER");

        useCase.execute(reservation.getId());

        Reservation cancelled =
                reservationRepository.findById(reservation.getId())
                        .orElseThrow();

        assertEquals(reservation.getId(), cancelled.getId());
        assertEquals(
                ReservationStatus.CANCELLED,
                cancelled.getStatus()
        );
    }

    @Test
    void shouldAllowNewReservationAfterCancellation() {
        User user = UserTestBuilder.builder().buildAndSave(userRepository);
        Calendar calendar = CalendarTestBuilder.builder().buildAndSave(calendarRepository);
        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);
        Reservation reservation = ReservationTestBuilder.builder()
                .withUserId(user.getId())
                .withTimeSlotId(timeSlot.getId())
                .buildAndSave(reservationRepository);

        authenticateAs(user.getId(), "USER");

        useCase.execute(reservation.getId());

        ReservationOutput output = createReservationUseCase.execute(
                new CreateReservationInput(
                        null,
                        timeSlot.getId(),
                        reservation.getReservationDate()
                )
        );

        assertNotNull(output.id());

        Reservation newReservation = reservationRepository
                .findById(output.id())
                .orElseThrow();

        assertEquals(timeSlot.getId(), newReservation.getTimeSlotId());
        assertEquals(
                reservation.getReservationDate(),
                newReservation.getReservationDate()
        );
        assertEquals(
                ReservationStatus.CONFIRMED,
                newReservation.getStatus()
        );

        List<Reservation> reservations = reservationRepository.findAll();

        long confirmedReservations = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .count();

        long cancelledReservations = reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CANCELLED)
                .count();

        assertAll(
                () -> assertEquals(2, reservations.size()),
                () -> assertEquals(1, confirmedReservations),
                () -> assertEquals(1, cancelledReservations)
        );
    }

    @Test
    void shouldAllowAdminToCancelAnotherUsersReservation() {
        User admin = UserTestBuilder.builder()
                .withEmail("admin@email.com")
                .withRole(UserRole.ADMIN)
                .buildAndSave(userRepository);

        User user = UserTestBuilder.builder()
                .withEmail("user@email.com")
                .buildAndSave(userRepository);

        Calendar calendar = CalendarTestBuilder.builder()
                .buildAndSave(calendarRepository);

        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);

        Reservation reservation = ReservationTestBuilder.builder()
                .withUserId(user.getId())
                .withTimeSlotId(timeSlot.getId())
                .buildAndSave(reservationRepository);

        authenticateAs(admin.getId(), "ADMIN");

        useCase.execute(reservation.getId());

        Reservation cancelled = reservationRepository
                .findById(reservation.getId())
                .orElseThrow();


        assertEquals(reservation.getId(), cancelled.getId());
        assertEquals(
                ReservationStatus.CANCELLED,
                cancelled.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenUserTriesToCancelAnotherUsersReservation() {
        User userA = UserTestBuilder.builder()
                .withEmail("userA@email.com")
                .buildAndSave(userRepository);

        User userB = UserTestBuilder.builder()
                .withEmail("userB@email.com")
                .buildAndSave(userRepository);

        Calendar calendar = CalendarTestBuilder.builder()
                .buildAndSave(calendarRepository);

        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);

        Reservation reservation = ReservationTestBuilder.builder()
                .withUserId(userA.getId())
                .withTimeSlotId(timeSlot.getId())
                .buildAndSave(reservationRepository);

        authenticateAs(userB.getId(), "USER");

        ForbiddenException exception =
                assertThrows(
                        ForbiddenException.class,
                        () -> useCase.execute(reservation.getId())
                );

        assertEquals(
                "You do not have permission to perform this action",
                exception.getMessage()
        );

        Reservation saved = reservationRepository
                .findById(reservation.getId())
                .orElseThrow();

        assertEquals(
                ReservationStatus.CONFIRMED,
                saved.getStatus()
        );
    }
}

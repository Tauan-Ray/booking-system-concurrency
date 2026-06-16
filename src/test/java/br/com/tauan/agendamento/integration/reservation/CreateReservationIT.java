package br.com.tauan.agendamento.integration.reservation;

import br.com.tauan.agendamento.IntegrationTest;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.reservation.application.dto.CreateReservationInput;
import br.com.tauan.agendamento.reservation.application.dto.ReservationOutput;
import br.com.tauan.agendamento.reservation.application.exception.ReservationConflictException;
import br.com.tauan.agendamento.reservation.application.usecase.CreateReservationUseCase;
import br.com.tauan.agendamento.reservation.domain.entity.Reservation;
import br.com.tauan.agendamento.reservation.domain.enums.ReservationStatus;
import br.com.tauan.agendamento.reservation.domain.repository.ReservationRepository;
import br.com.tauan.agendamento.test.factory.CalendarTestBuilder;
import br.com.tauan.agendamento.test.factory.TimeSlotTestBuilder;
import br.com.tauan.agendamento.test.factory.UserTestBuilder;
import br.com.tauan.agendamento.timeslot.domain.entity.TimeSlot;
import br.com.tauan.agendamento.timeslot.domain.repository.TimeSlotRepository;
import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.enums.UserRole;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreateReservationIT extends IntegrationTest {
    @Autowired
    private CreateReservationUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void shouldCreateReservationSuccessfully() {
        User user = UserTestBuilder.builder().buildAndSave(userRepository);
        Calendar calendar = CalendarTestBuilder.builder().buildAndSave(calendarRepository);
        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);

        authenticateAs(user.getId(), "USER");

        CreateReservationInput input = new CreateReservationInput(
                null,
                timeSlot.getId(),
                LocalDate.now().plusDays(1)
        );

        ReservationOutput output = useCase.execute(input);

        assertNotNull(output.id());
        assertEquals(user.getId(), output.userId());
        assertEquals(timeSlot.getId(), output.timeSlotId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertEquals(1, reservations.size());

        Reservation saved = reservations.getFirst();
        assertEquals(ReservationStatus.CONFIRMED, saved.getStatus());
    }

    @Test
    void shouldCreateReservationForAnotherUserWhenRequesterIsAdmin() {
        User admin = UserTestBuilder.builder()
                .withEmail("admin@email.com")
                .withRole(UserRole.ADMIN)
                .buildAndSave(userRepository);

        User targetUser = UserTestBuilder.builder()
                .withEmail("user@email.com")
                .buildAndSave(userRepository);

        Calendar calendar = CalendarTestBuilder.builder().buildAndSave(calendarRepository);
        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);

        authenticateAs(admin.getId(), "ADMIN");

        CreateReservationInput input = new CreateReservationInput(
                targetUser.getId(),
                timeSlot.getId(),
                LocalDate.now().plusDays(1)
        );

        ReservationOutput output = useCase.execute(input);

        assertEquals(targetUser.getId(), output.userId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertEquals(1, reservations.size());

        Reservation saved = reservations.getFirst();
        assertEquals(targetUser.getId(), saved.getUserId());
        assertEquals(ReservationStatus.CONFIRMED, saved.getStatus());
    }

    @Test
    void shouldOverrideUserIdWithAuthenticatedUserWhenNotAdmin() {

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

        authenticateAs(userA.getId(), "USER");

        CreateReservationInput input = new CreateReservationInput(
                userB.getId(),
                timeSlot.getId(),
                LocalDate.now().plusDays(1)
        );

        ReservationOutput output = useCase.execute(input);

        assertEquals(userA.getId(), output.userId());

        List<Reservation> reservations = reservationRepository.findAll();
        assertEquals(1, reservations.size());

        Reservation saved = reservations.getFirst();

        assertNotEquals(userB.getId(), saved.getUserId());
        assertEquals(userA.getId(), saved.getUserId());

        assertEquals(ReservationStatus.CONFIRMED, saved.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenReservationConflicts() {
        User user = UserTestBuilder.builder().buildAndSave(userRepository);
        Calendar calendar = CalendarTestBuilder.builder().buildAndSave(calendarRepository);
        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);

        authenticateAs(user.getId(), "USER");

        CreateReservationInput firstInput = new CreateReservationInput(
                null,
                timeSlot.getId(),
                LocalDate.now().plusDays(1)
        );

        CreateReservationInput secondInput = new CreateReservationInput(
                null,
                timeSlot.getId(),
                LocalDate.now().plusDays(1)
        );

        useCase.execute(firstInput);
        ReservationConflictException exception =
                assertThrows(
                        ReservationConflictException.class,
                        () -> useCase.execute(secondInput)
                );

        assertEquals(
                "Time slot is already reserved for this date",
                exception.getMessage()
        );

        assertEquals(1, reservationRepository.findAll().size());
    }
}

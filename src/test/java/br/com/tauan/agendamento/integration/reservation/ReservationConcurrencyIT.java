package br.com.tauan.agendamento.integration.reservation;

import br.com.tauan.agendamento.IntegrationTest;
import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;
import br.com.tauan.agendamento.reservation.application.dto.CreateReservationInput;
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
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationConcurrencyIT extends IntegrationTest {

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
    void shouldAllowOnlyOneReservationWhenRequestsAreConcurrent() throws Exception {
        User userA = UserTestBuilder.builder()
                .withEmail("userA@email.com")
                .buildAndSave(userRepository);

        User userB = UserTestBuilder.builder()
                .withEmail("userB@email.com")
                .buildAndSave(userRepository);

        User userC = UserTestBuilder.builder()
                .withEmail("userC@email.com")
                .buildAndSave(userRepository);

        User userD = UserTestBuilder.builder()
                .withEmail("userD@email.com")
                .buildAndSave(userRepository);

        Calendar calendar = CalendarTestBuilder.builder()
                .buildAndSave(calendarRepository);

        TimeSlot timeSlot = TimeSlotTestBuilder.builder()
                .withCalendarId(calendar.getId())
                .buildAndSave(timeSlotRepository);

        LocalDate reservationDate = LocalDate.now().plusDays(1);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CountDownLatch start = new CountDownLatch(1);

        Future<Boolean> userAResult = executor.submit(
                createReservationTask(
                        userA.getId(),
                        timeSlot.getId(),
                        reservationDate,
                        start
                )
        );

        Future<Boolean> userBResult = executor.submit(
                createReservationTask(
                        userB.getId(),
                        timeSlot.getId(),
                        reservationDate,
                        start
                )
        );

        Future<Boolean> userCResult = executor.submit(
                createReservationTask(
                        userC.getId(),
                        timeSlot.getId(),
                        reservationDate,
                        start
                )
        );

        Future<Boolean> userDResult = executor.submit(
                createReservationTask(
                        userD.getId(),
                        timeSlot.getId(),
                        reservationDate,
                        start
                )
        );

        start.countDown();

        int successCount =
                (userAResult.get() ? 1 : 0) +
                (userBResult.get() ? 1 : 0) +
                (userCResult.get() ? 1 : 0) +
                (userDResult.get() ? 1 : 0);


        List<Reservation> reservations = reservationRepository.findAll();
        assertEquals(1, successCount);

        assertEquals(
                1,
                reservations
                        .stream()
                        .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                        .count()
        );

        assertEquals(1, reservations.size());
    }

    private Callable<Boolean> createReservationTask(
            UUID userId,
            UUID timeSlotId,
            LocalDate reservationDate,
            CountDownLatch start
    ) {
        return () -> {
            start.await();

            authenticateAs(userId, "USER");

            try {
                useCase.execute(
                        new CreateReservationInput(
                                null,
                                timeSlotId,
                                reservationDate
                        )
                );

                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }
}

package com.paxtech.utime.platform.notifications.application.internal.scheduling;

import com.paxtech.utime.platform.notifications.domain.model.commands.CreateReminderCommand;
import com.paxtech.utime.platform.notifications.domain.model.valueobjects.RecipientType;
import com.paxtech.utime.platform.notifications.domain.services.ReminderCommandService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.TimeSlotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UE02 - Automatic appointment reminders.
 * Periodically scans reservations whose appointment starts within the next
 * 24 hours and, for each one, generates an in-app reminder for both the
 * client and the provider. Reminder creation is idempotent, so running on a
 * fixed rate never produces duplicates.
 */
@Service
public class ReminderSchedulerService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
    private static final long REMINDER_WINDOW_HOURS = 24;

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ReminderCommandService reminderCommandService;

    public ReminderSchedulerService(ReservationRepository reservationRepository,
                                    TimeSlotRepository timeSlotRepository,
                                    ReminderCommandService reminderCommandService) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.reminderCommandService = reminderCommandService;
    }

    @Scheduled(fixedRate = 60_000)
    public void generateUpcomingReminders() {
        var now = LocalDateTime.now();
        var windowEnd = now.plusHours(REMINDER_WINDOW_HOURS);

        reservationRepository.findAll().forEach(reservation ->
                timeSlotRepository.findById(reservation.getTimeSlotId()).ifPresent(timeSlot -> {
                    var start = timeSlot.getStartTime();
                    if (start.isAfter(now) && start.isBefore(windowEnd)) {
                        var when = start.format(FORMATTER);

                        reminderCommandService.handle(new CreateReminderCommand(
                                reservation.getId(),
                                RecipientType.CLIENT,
                                reservation.getClientId(),
                                "Recordatorio: tienes una cita programada para el " + when + ". ¡No la olvides!",
                                start));

                        reminderCommandService.handle(new CreateReminderCommand(
                                reservation.getId(),
                                RecipientType.PROVIDER,
                                reservation.getProviderId(),
                                "Recordatorio: tienes una reserva programada para el " + when + ".",
                                start));
                    }
                }));
    }
}

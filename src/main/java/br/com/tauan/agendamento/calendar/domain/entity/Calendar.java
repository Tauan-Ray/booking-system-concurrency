package br.com.tauan.agendamento.calendar.domain.entity;

import br.com.tauan.agendamento.calendar.domain.exception.CalendarAlreadyDeletedException;
import br.com.tauan.agendamento.calendar.domain.exception.InvalidCalendarException;
import br.com.tauan.agendamento.shared.domain.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Calendar extends BaseEntity {
    private String name;

    private Calendar(
            UUID id,
            String name,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        super(
                id,
                createdAt,
                updatedAt,
                deletedAt
        );

        this.name = name;
    }

    public static Calendar create(String name) {
        LocalDateTime now = LocalDateTime.now();

        validate(name);

        return new Calendar(
                UUID.randomUUID(),
                name,
                now,
                now,
                null
        );
    }

    public static Calendar restore(
            UUID id,
            String name,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        validate(name);

        return new Calendar(
                id,
                name,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    private static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidCalendarException("Name cannot be empty");
        }
    }

    public void archive() {
        if (isDeleted()) {
            throw new CalendarAlreadyDeletedException();
        }

        this.deletedAt = LocalDateTime.now();
        touch();
    }

    public String getName() {
        return name;
    }
}


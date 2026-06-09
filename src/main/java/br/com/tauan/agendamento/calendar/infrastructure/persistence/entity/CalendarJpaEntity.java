package br.com.tauan.agendamento.calendar.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "calendars",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_calendars_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CalendarJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_at",nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

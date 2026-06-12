package br.com.tauan.agendamento.user.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserOutput(
        UUID id,
        String name,
        String email,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {}

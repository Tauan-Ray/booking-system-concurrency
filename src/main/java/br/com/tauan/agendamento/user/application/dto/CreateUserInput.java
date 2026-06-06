package br.com.tauan.agendamento.user.application.dto;

public record CreateUserInput(
        String name,
        String email,
        String password
) {}

package br.com.tauan.agendamento.user.domain.entity;

import br.com.tauan.agendamento.shared.domain.entity.BaseEntity;
import br.com.tauan.agendamento.user.domain.enums.UserRole;
import br.com.tauan.agendamento.user.domain.exception.InvalidUserException;
import br.com.tauan.agendamento.user.domain.exception.UserAlreadyDeletedException;
import br.com.tauan.agendamento.user.domain.valueobject.Email;

import java.time.LocalDateTime;
import java.util.UUID;

public class User extends BaseEntity {
    private String name;
    private Email email;
    private String password;
    private UserRole role;

    private User(
            UUID id,
            String name,
            Email email,
            String password,
            UserRole role,
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
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User create(String name, Email email, String password) {
        LocalDateTime now = LocalDateTime.now();

        if (name == null || name.isBlank()) {
            throw new InvalidUserException("Name cannot be empty");
        }

        if (password == null || password.isBlank()) {
            throw new InvalidUserException("Password cannot be empty");
        }

        return new User(
                UUID.randomUUID(),
                name,
                email,
                password,
                UserRole.USER,
                now,
                now,
                null
        );
    }

    public void deactivate() {
        if (isDeleted()) {
            throw new UserAlreadyDeletedException();
        }

        this.deletedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public Email getEmail() {
        return email;
    }
}

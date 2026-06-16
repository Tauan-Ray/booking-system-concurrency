package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.enums.UserRole;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import br.com.tauan.agendamento.user.domain.valueobject.Email;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTestBuilder {

    private String name = "Tauan";
    private String email = "tauan@email.com";
    private String password = "123456";
    private UserRole role = UserRole.USER;

    public static UserTestBuilder builder() {
        return new UserTestBuilder();
    }

    public UserTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserTestBuilder withRole(UserRole role) {
        this.role = role;
        return this;
    }

    public User build() {
        return User.restore(
                UUID.randomUUID(),
                name,
                new Email(email),
                password,
                role,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public User buildAndSave(UserRepository repository) {
        return repository.save(build());
    }
}

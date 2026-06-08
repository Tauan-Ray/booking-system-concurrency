package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.valueobject.Email;

public class UserTestBuilder {

    private String name = "Tauan";
    private String email = "tauan@email.com";
    private String password = "123456";

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

    public User build() {
        return User.create(
                name,
                new Email(email),
                password
        );
    }
}

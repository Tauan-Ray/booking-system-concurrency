package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.user.application.dto.CreateUserInput;

public class UserInputTestBuilder {

    private String name = "Tauan";
    private String email = "tauan@email.com";
    private String password = "123456";

    public static UserInputTestBuilder builder() {
        return new UserInputTestBuilder();
    }

    public UserInputTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserInputTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserInputTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CreateUserInput build() {
        return new CreateUserInput(
                name,
                email,
                password
        );
    }
}

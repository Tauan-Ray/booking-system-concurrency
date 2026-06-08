package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.user.application.dto.CreateUserInput;
import br.com.tauan.agendamento.user.application.dto.LoginInput;

public class LoginInputTestBuilder {
    private String email = "tauan@email.com";
    private String password = "123456";

    public static LoginInputTestBuilder builder() {
        return new LoginInputTestBuilder();
    }

    public LoginInputTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginInputTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginInput build() {
        return new LoginInput(
                email,
                password
        );
    }
}

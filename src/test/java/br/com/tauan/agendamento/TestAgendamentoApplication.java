package br.com.tauan.agendamento;

import org.springframework.boot.SpringApplication;

public class TestAgendamentoApplication {

	public static void main(String[] args) {
		SpringApplication.from(AgendamentoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

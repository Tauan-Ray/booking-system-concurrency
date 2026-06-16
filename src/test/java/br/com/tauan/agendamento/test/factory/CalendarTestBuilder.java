package br.com.tauan.agendamento.test.factory;

import br.com.tauan.agendamento.calendar.domain.entity.Calendar;
import br.com.tauan.agendamento.calendar.domain.repository.CalendarRepository;

public class CalendarTestBuilder {

    private String name = "Default Calendar";

    public static CalendarTestBuilder builder() {
        return new CalendarTestBuilder();
    }

    public CalendarTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Calendar build() {
        return Calendar.create(name);
    }

    public Calendar buildAndSave(CalendarRepository repository) {
        return repository.save(build());
    }
}
package br.com.tauan.agendamento.user.domain.repository;

import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.valueobject.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(Email email);
    User save(User user);
}

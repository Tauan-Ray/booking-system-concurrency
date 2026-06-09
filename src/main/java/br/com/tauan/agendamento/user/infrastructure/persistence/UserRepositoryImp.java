package br.com.tauan.agendamento.user.infrastructure.persistence;

import br.com.tauan.agendamento.user.domain.entity.User;
import br.com.tauan.agendamento.user.domain.repository.UserRepository;
import br.com.tauan.agendamento.user.domain.valueobject.Email;
import br.com.tauan.agendamento.user.infrastructure.persistence.entity.UserJpaEntity;
import br.com.tauan.agendamento.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import br.com.tauan.agendamento.user.infrastructure.persistence.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImp implements UserRepository {

    private final SpringDataUserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByIdIncludingDeleted(UUID id) {
        return repository.findById(id)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repository.findByEmail(email.getValue())
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity saved = repository.save(
                UserPersistenceMapper.toJpaEntity(user)
        );

        return UserPersistenceMapper.toDomain(saved);
    }
}

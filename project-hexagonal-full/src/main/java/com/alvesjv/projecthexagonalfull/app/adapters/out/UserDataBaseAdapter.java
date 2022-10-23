package com.alvesjv.projecthexagonalfull.app.adapters.out;

import com.alvesjv.projecthexagonalfull.app.adapters.out.repository.UserRepository;
import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.ports.out.DataBaseIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Qualifier("user")
public class UserDataBaseAdapter implements DataBaseIntegration<User, UUID> {

    @Autowired
    private UserRepository repository;

    @Override
    public User save(User user) {
        return repository.saveAndFlush(user);
    }

    @Override
    @Cacheable(value = "findById", key = "#id", unless = "#result == null")
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public List<User> findAll(Example<User> example) {
        return repository.findAll(example);
    }

    @Override
    public Optional<User> findOne(Example<User> example) {
        return repository.findOne(example);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}

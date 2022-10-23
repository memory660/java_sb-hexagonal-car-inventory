package com.alvesjv.projecthexagonalfull.app.adapters.out;

import com.alvesjv.projecthexagonalfull.app.adapters.out.repository.CarRepository;
import com.alvesjv.projecthexagonalfull.app.domain.entities.Car;
import com.alvesjv.projecthexagonalfull.app.ports.out.DataBaseIntegration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@Slf4j
@Qualifier("car")
public class CarDataBaseAdapter implements DataBaseIntegration<Car, UUID> {

    @Autowired
    private CarRepository repository;

    @Override
    public Car save(Car car) {
        return repository.saveAndFlush(car);
    }

    @Override
    @Cacheable(value = "findById", key = "#id", unless="#result == null")
    public Optional<Car> findById(UUID id) {
        log.info("------------------------------Pesquisou no banco----------------------------------");
        return repository.findById(id);
    }

    @Override
    public List<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Car> findAll(Example<Car> example) {
        return repository.findAll(example);
    }

    @Override
    public Optional<Car> findOne(Example<Car> example) {
        return findOne(example);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}

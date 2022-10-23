package com.alvesjv.projecthexagonalfull.app.ports.out;

import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface DataBaseIntegration<T, ID> {

    public T save(T t);
    public Optional<T> findById(ID id);
    public List<T> findAll();
    public List<T> findAll(Example<T> example);
    public Optional<T> findOne(Example<T> example);
    public void deleteById(ID id);
}

package com.alvesjv.projecthexagonalfull.app.adapters.out.repository;

import com.alvesjv.projecthexagonalfull.app.domain.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
}

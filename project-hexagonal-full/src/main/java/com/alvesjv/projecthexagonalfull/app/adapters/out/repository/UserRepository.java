package com.alvesjv.projecthexagonalfull.app.adapters.out.repository;

import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}

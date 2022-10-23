package com.alvesjv.projecthexagonalfull.app.domain.entities;

import com.alvesjv.projecthexagonalfull.app.domain.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User implements Serializable {
    @Id
    private UUID idUser;
    private String username;
    private String password;
    private Role role;
}

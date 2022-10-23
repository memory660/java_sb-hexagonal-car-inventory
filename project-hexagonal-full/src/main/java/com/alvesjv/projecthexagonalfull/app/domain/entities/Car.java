package com.alvesjv.projecthexagonalfull.app.domain.entities;

import com.alvesjv.projecthexagonalfull.app.domain.enums.Status;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Car  implements Comparable<Car>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCar;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime inclusionDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime modificationDate;

    private String name;

    @Size(min=3, message="Enter at least 3 characters")
    private String brand;

    @Size(min=7, max=7, message="Enter at least 7 characters")
    private String licensePlate;

    private Status status;

    public int compareTo(Car o){
        return this.status.compareTo(o.status);
    }
}

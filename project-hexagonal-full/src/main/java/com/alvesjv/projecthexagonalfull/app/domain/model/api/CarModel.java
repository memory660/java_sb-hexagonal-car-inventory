package com.alvesjv.projecthexagonalfull.app.domain.model.api;

import com.alvesjv.projecthexagonalfull.app.domain.enums.Status;
import com.alvesjv.projecthexagonalfull.app.domain.model.api.UserModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class CarModel {
    private UUID idCar;
    private UserModel user;
    private LocalDateTime inclusionDate;
    private LocalDateTime modificationDate;

    @NotEmpty
    private String name;

    @NotEmpty
    @Size(min=3, message="Enter at least 3 characters")
    private String brand;

    @NotEmpty
    @Size(min=7,max=7, message="Enter at least 7 characters")
    private String licensePlate;

    @NotNull
    private Status status;

}

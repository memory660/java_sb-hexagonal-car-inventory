package com.alvesjv.projecthexagonalfull.app.utils;

import com.alvesjv.projecthexagonalfull.app.domain.entities.Car;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Role;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Status;
import com.alvesjv.projecthexagonalfull.app.domain.model.api.CarModel;
import com.alvesjv.projecthexagonalfull.app.domain.model.api.UserModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CarTestUtils {

    public static CarModel createCarModel(){
        CarModel carModel = new CarModel();
        UserModel user1 = new UserModel();
        user1.setIdUser(UUID.randomUUID());

        carModel.setIdCar(UUID.randomUUID());
        carModel.setBrand("FIAT");
        carModel.setLicensePlate("BVQ3272");
        carModel.setName("UNO");
        carModel.setStatus(Status.WAITING);
        carModel.setUser(user1);

        return carModel;
    }

    public  static List<CarModel> createListCarModel(){
        return Arrays.asList(createCarModel());
    }

    public static Car createCar(Role role){
        Car car = new Car();

        car.setIdCar(UUID.randomUUID());
        car.setBrand("Volkswagem");
        car.setLicensePlate("BVQ2255");
        car.setName("JETTA");
        car.setStatus(Status.WAITING);
        car.setInclusionDate(LocalDateTime.now().minusDays(2));
        car.setModificationDate(LocalDateTime.now());
        car.setUser(UserTestUtils.createUser(role));

        return car;
    }

    public static List<Car> createListCar(){
        return Arrays.asList(createCar(Role.USER));
    }
}

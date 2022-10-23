package com.alvesjv.projecthexagonalfull.app.domain.core;

import com.alvesjv.projecthexagonalfull.app.domain.entities.Car;
import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Role;
import com.alvesjv.projecthexagonalfull.app.domain.enums.Status;
import com.alvesjv.projecthexagonalfull.app.domain.exception.CarException;
import com.alvesjv.projecthexagonalfull.app.domain.exception.CustomException;
import com.alvesjv.projecthexagonalfull.app.domain.mapper.CarMapper;
import com.alvesjv.projecthexagonalfull.app.domain.model.api.CarModel;
import com.alvesjv.projecthexagonalfull.app.ports.out.DataBaseIntegration;
import com.alvesjv.projecthexagonalfull.app.utils.CarTestUtils;
import com.alvesjv.projecthexagonalfull.app.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CarCoreTest {

    @InjectMocks
    private CarCore carCore;

    @Mock
    private DataBaseIntegration carDataBase;

    @Mock
    private DataBaseIntegration userDataBase;

    @Mock
    private SecurityCore securityCore;

    private  CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @BeforeEach
    public void before(){
        ReflectionTestUtils.setField(carCore, "carMapper", carMapper);
    }

    @Test
    public void mustCreateCar(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        CarModel carModel = CarTestUtils.createCarModel();
        Car car = carMapper.mapper(carModel, user);

        when(carDataBase.save(any(Car.class))).thenReturn(car);

        CarModel carsSaved = carCore.create(carModel);

        assertEquals(carModel.getIdCar(), carsSaved.getIdCar());
        assertEquals(carModel.getBrand(), carsSaved.getBrand());
        assertEquals(carModel.getLicensePlate(), carsSaved.getLicensePlate());
    }

    @Test
    public void mustGenerateExceptionOnCreateCar(){
        CarModel carModel = CarTestUtils.createCarModel();
        User user = UserTestUtils.createUser(Role.USER);

        when(carDataBase.save(any(Car.class))).thenThrow(new CustomException(new Throwable("error")));
        when(securityCore.getCurrentUser()).thenReturn(user);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.create(carModel),
                "error");

        assertTrue(ex.getMessage().contains("error"));
    }

    @Test
    public void mustFindCar(){

        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));

        User user = car.getUser();//UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);

        CarModel carFound =  carCore.find(UUID.randomUUID().toString());
        assertEquals(car.getIdCar(), carFound.getIdCar());
        assertEquals(car.getBrand(), carFound.getBrand());
        assertEquals(car.getLicensePlate(), carFound.getLicensePlate());
        assertEquals(car.getUser().getIdUser(), carFound.getUser().getIdUser());
        assertEquals(car.getName(), carFound.getName());
        assertEquals(car.getStatus(), carFound.getStatus());
    }

    @Test
    public void mustFindCarForAdmin(){

        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));

        User user = UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);

        CarModel carFound =  carCore.find(UUID.randomUUID().toString());
        assertEquals(car.getIdCar(), carFound.getIdCar());
        assertEquals(car.getBrand(), carFound.getBrand());
        assertEquals(car.getLicensePlate(), carFound.getLicensePlate());
        assertEquals(car.getUser().getIdUser(), carFound.getUser().getIdUser());
        assertEquals(car.getName(), carFound.getName());
        assertEquals(car.getStatus(), carFound.getStatus());
    }

    @Test
    public void mustGenerateExceptionNotAuthorizedFindCar(){

        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));

        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.find(UUID.randomUUID().toString()),
                "Not authorized");

        assertTrue(ex.getMessage().contains("Not authorized"));
    }

    @Test
    public void mustNotFoundCar(){

        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.find(UUID.randomUUID().toString()),
                "car not found");

        assertTrue(ex.getMessage().contains("car not found"));
    }

    @Test
    public void mustDeleteCarForUser(){

        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        Car car = CarTestUtils.createCar(Role.USER);
        car.setUser(user);

        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));

        assertDoesNotThrow(()-> carCore.delete(car.getIdCar().toString()));
    }

    @Test
    public void mustGenerateExceptionCarNotFoundOnDelete(){

        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        Car car = CarTestUtils.createCar(Role.USER);
        car.setUser(user);

        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.delete(UUID.randomUUID().toString()),
                "car not found");

        assertTrue(ex.getMessage().contains("car not found"));
    }

    @Test
    public void mustGenerateExceptionNotAuthorizedDeleteCar(){

        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));

        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.delete(UUID.randomUUID().toString()),
                "Not authorized");

        assertTrue(ex.getMessage().contains("Not authorized"));
    }

    @Test
    public void mustUpdateCar(){

        User user = UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);
        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));
        CarModel carModel =  carCore.find(car.getIdCar().toString());

        assertDoesNotThrow(()-> carCore.update(car.getIdCar().toString(), carModel));

    }

    @Test
    public void mustUpdateNotFoundCar(){

        User user = UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);

        Car car = CarTestUtils.createCar(Role.USER);
        car.setUser(user);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.update(UUID.randomUUID().toString(), CarTestUtils.createCarModel()),
                "car not found");

        assertTrue(ex.getMessage().contains("car not found"));
    }

    @Test
    public void mustUpdateNotAuthorizedCar(){

        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        Car car = CarTestUtils.createCar(Role.USER);
        when(carDataBase.findById(any(UUID.class))).thenReturn(Optional.of(car));

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.update(car.getIdCar().toString(), CarTestUtils.createCarModel()),
                "Not authorized");
        assertTrue(ex.getMessage().contains("Not authorized"));
    }

    @Test
    public void mustListAllCarsForAdmin(){
        User user = UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);
        when(carDataBase.findAll()).thenReturn(CarTestUtils.createListCar());

        assertDoesNotThrow(()-> carCore.list(null));
        List<CarModel> cars = carCore.list(null);
        assertFalse(cars.isEmpty());
    }

    @Test
    public void mustListAllCarsForUser(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        List<Car> cars = CarTestUtils.createListCar();
        user.setIdUser(cars.get(0).getUser().getIdUser());
        when(carDataBase.findAll(any(Example.class))).thenReturn(cars);

        assertDoesNotThrow(()-> carCore.list(null));
        assertFalse(cars.isEmpty());
    }

    @Test
    public void mustGenerateNoCarsRegistredForUser(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        List<Car> cars = CarTestUtils.createListCar();
        user.setIdUser(cars.get(0).getUser().getIdUser());
        when(carDataBase.findAll(any(Example.class))).thenReturn(null);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.list(null),
                "No cars registered");
        assertTrue(ex.getMessage().contains("No cars registered"));
    }

    @Test
    public void mustGenerateExceptionFindCarsWithStatusNotFound(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.list("testStatus"),
                "Status not found");
        assertTrue(ex.getMessage().contains("Status not found"));
    }

    @Test
    public void mustGenerateExceptionAdminFindCarsWithStatusWaiting(){
        User user = UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);
        when(carDataBase.findAll(any(Example.class))).thenReturn(CarTestUtils.createListCar());

        List<CarModel> cars = carCore.list("WAITING");
        assertFalse(cars.isEmpty());
        assertTrue(cars.get(0).getStatus().equals(Status.WAITING));
    }

    @Test
    public void mustGenerateExceptionAdminFindCarsWithStatusIncluded(){
        User user = UserTestUtils.createUser(Role.ADMIN);
        when(securityCore.getCurrentUser()).thenReturn(user);
        when(carDataBase.findAll(any(Example.class))).thenReturn(null);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.list("INCLUDED"),
                "No cars registered with status INCLUDED");
        assertTrue(ex.getMessage().contains("No cars registered with status INCLUDED"));
    }

    @Test
    public void mustGenerateExceptionUserFindCarsWithStatusWaiting(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);
        
        List<Car> cars = CarTestUtils.createListCar();
        cars.get(0).setUser(user);
        when(carDataBase.findAll(any(Example.class))).thenReturn(cars);

        List<CarModel> carModel = carCore.list("WAITING");
        assertFalse(carModel.isEmpty());
        assertTrue(carModel.get(0).getStatus().equals(Status.WAITING));

    }

    @Test
    public void mustGenerateExceptionUserFindCarsWithStatusIncluded(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);
        when(carDataBase.findAll(any(Example.class))).thenReturn(null);

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.list("INCLUDED"),
                "No cars registered with status INCLUDED");
        assertTrue(ex.getMessage().contains("No cars registered with status INCLUDED"));
    }

    @Test
    public void mustGenerateExceptionUserFindCarsWithStatusIncludedIsEmpty(){
        User user = UserTestUtils.createUser(Role.USER);
        when(securityCore.getCurrentUser()).thenReturn(user);
        when(carDataBase.findAll(any(Example.class))).thenReturn(new ArrayList());

        CustomException ex = assertThrows(CustomException.class,
                ()->carCore.list("INCLUDED"),
                "No cars registered with status INCLUDED");
        assertTrue(ex.getMessage().contains("No cars registered with status INCLUDED"));

    }

}
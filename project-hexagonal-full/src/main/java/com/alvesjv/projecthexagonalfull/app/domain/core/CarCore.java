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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CarCore {

    @Autowired
    @Qualifier("car")
    private DataBaseIntegration carDataBase;

    @Autowired
    @Qualifier("user")
    private DataBaseIntegration userDataBase;

    @Autowired
    private SecurityCore securityCore;

    @Autowired
    private CarMapper carMapper;

    public CarModel create(CarModel carModel){
        try{

            User userOp = securityCore.getCurrentUser();
            Car car = carMapper.mapper(carModel, userOp);

            Car newCar = (Car) carDataBase.save(car);

            CarModel model = carMapper.mapper(newCar);

            log.info("car created successfully - {}", model);

            return model;
        }catch(Throwable t){
            log.error("Error create carModel: ", t);
            throw new CustomException(t);
        }
    }

    public CarModel find(String id){
        try{
            UUID uuid = UUID.fromString(id);
            Optional<Car> opCar = carDataBase.findById(uuid);

            if(!opCar.isPresent()){
                throw new CarException("car not found", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            User user = securityCore.getCurrentUser();

            if(user.getRole().equals(Role.ADMIN)){
                CarModel model = carMapper.mapper(opCar.get());
                log.info("opcar found successfully - {}", model);

                return model;
            }

            if (!opCar.get().getUser().getIdUser().equals(user.getIdUser())){
                throw new CarException("Not authorized", HttpStatus.FORBIDDEN);
            }

            CarModel model = carMapper.mapper(opCar.get());

            log.info("opcar found successfully - {}", model);

            return model;

        }catch(Throwable t){
            log.error("Error find car: ", t);
            throw new CustomException(t);
        }
    }

    public List<CarModel> list(String status){
        try{
            User user = securityCore.getCurrentUser();
            List<Car> cars = null;

            if(Objects.isNull(status)){
                if(user.getRole().equals(Role.ADMIN)){
                    cars = carDataBase.findAll();
                }

                if(user.getRole().equals(Role.USER)){
                    Car carFilter = new Car();
                    carFilter.setUser(user);
                    Example<Car> ex = Example.of(carFilter);
                    cars = carDataBase.findAll(ex);
                }

                if(Objects.isNull(cars) || cars.isEmpty()){
                    throw new CarException("No cars registered", HttpStatus.UNPROCESSABLE_ENTITY);
                }

                Collections.sort(cars);
                List<CarModel> models = carMapper.mapper(cars);

                log.info("cars listed successfully -{}", models);

                return models;
            }

            Status statusFilter = Status.fromString(status);
            if(Objects.isNull(statusFilter)){
                throw new CarException("Status not found", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Car carFilter = new Car();
            carFilter.setStatus(statusFilter);

            if(user.getRole().equals(Role.ADMIN)){
                Example<Car> ex = Example.of(carFilter);

                cars = carDataBase.findAll(ex);

                if(Objects.isNull(cars) || cars.isEmpty()){
                    throw new CarException("No cars registered with status ".concat(status.toString()), HttpStatus.BAD_REQUEST);
                }

                Collections.sort(cars);
                List<CarModel> models = carMapper.mapper(cars);

                log.info("cars listed successfully -{}", models);

                return models;
            }

            carFilter.setUser(user);

            Example<Car> ex = Example.of(carFilter);
            cars = carDataBase.findAll(ex);

            if(Objects.isNull(cars) || cars.isEmpty()){
                throw new CarException("No cars registered with status ".concat(status.toString()),
                        HttpStatus.BAD_REQUEST);
            }

            List<CarModel> models = carMapper.mapper(cars);

            log.info("cars listed successfully -{}", models);

            return models;

        }catch(Throwable t){
            log.error("Error list cars by status: {}", t);
            throw new CustomException(t);
        }
    }

    public CarModel update(String id, CarModel car){
        try{
            User user = securityCore.getCurrentUser();
            UUID uuid = UUID.fromString(id);
            Optional<Car> opcar = carDataBase.findById(uuid);

            if(!opcar.isPresent()){
                throw new CarException("car not found", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if(user.getRole().equals(Role.USER) && !opcar.get().getUser().getIdUser().equals(user.getIdUser())){
                throw new CarException("Not authorized", HttpStatus.FORBIDDEN);
            }

            Car carUpdate = opcar.get();
            if(!Objects.isNull(car.getName())){
                carUpdate.setName(car.getName());
            }

            if(!Objects.isNull(car.getBrand())){
                carUpdate.setBrand(car.getBrand());
            }

            if(!Objects.isNull(car.getLicensePlate())){
                carUpdate.setLicensePlate(car.getLicensePlate());
                carUpdate.setModificationDate(LocalDateTime.now());
            }

            carDataBase.save(carUpdate);

            CarModel model = carMapper.mapper(carUpdate);
            log.info("car updated successfully - {}", model);

            return model;
        }
        catch(Throwable t){
            log.error("Error update car: {}", t);
            throw new CustomException(t);
        }
    }

    public void delete(final String id){
        try{
            User user = securityCore.getCurrentUser();
            UUID uuid = UUID.fromString(id);
            Optional<Car> opcar = carDataBase.findById(uuid);

            if(!opcar.isPresent()){
                throw new CarException("car not found", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if(user.getRole().equals(Role.USER) && !opcar.get().getUser().getIdUser().equals(user.getIdUser())){
                throw new CarException("Not authorized", HttpStatus.FORBIDDEN);
            }

            carDataBase.deleteById(uuid);
            log.info("car deleted successfully");
        }
        catch(Throwable t){
            log.error("Error delete cars by id: {}", t);
            throw new CustomException(t);
        }
    }

}

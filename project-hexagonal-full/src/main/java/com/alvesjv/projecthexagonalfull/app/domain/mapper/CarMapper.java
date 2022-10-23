package com.alvesjv.projecthexagonalfull.app.domain.mapper;

import com.alvesjv.projecthexagonalfull.app.domain.entities.Car;
import com.alvesjv.projecthexagonalfull.app.domain.entities.User;
import com.alvesjv.projecthexagonalfull.app.domain.model.api.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mappings({
            @Mapping(source = "u", target = "user"),
            @Mapping(target = "inclusionDate", expression = "java(inclusionDate())")
    })
    Car mapper(CarModel car, User u);

    CarModel mapper(Car car);

    List<CarModel> mapper(List<Car> car);

    default LocalDateTime inclusionDate(){
        return LocalDateTime.now();
    }

}

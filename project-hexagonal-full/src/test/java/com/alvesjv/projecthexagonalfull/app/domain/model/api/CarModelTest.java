package com.alvesjv.projecthexagonalfull.app.domain.model.api;

import com.alvesjv.projecthexagonalfull.app.utils.CarTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pojo.tester.api.assertion.Assertions;
import pl.pojo.tester.api.assertion.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CarModelTest {
    @Test
    public void testModel(){
        Assertions.assertPojoMethodsFor(CarModel.class).testing(Method.GETTER, Method.SETTER, Method.TO_STRING).areWellImplemented();
        CarModel testModel = CarTestUtils.createCarModel();
        assertFalse(testModel.toString().isEmpty());
    }
}
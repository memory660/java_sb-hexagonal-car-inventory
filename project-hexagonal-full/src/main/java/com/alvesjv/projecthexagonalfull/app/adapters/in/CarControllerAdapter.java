package com.alvesjv.projecthexagonalfull.app.adapters.in;

import com.alvesjv.projecthexagonalfull.app.domain.core.CarCore;
import com.alvesjv.projecthexagonalfull.app.domain.model.api.CarModel;
import com.alvesjv.projecthexagonalfull.app.ports.in.CarPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/cars")
public class CarControllerAdapter implements CarPort {

    @Autowired
    private CarCore core;

    @Override
    @PostMapping
    public ResponseEntity<CarModel> create(@RequestBody @Valid CarModel car) {
        CarModel t = core.create(car);
        return new ResponseEntity<>(t, HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CarModel> find(@PathVariable String id) {
        CarModel t = core.find(id);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CarModel>> list(@RequestParam(required = false) String status) {
        List<CarModel> t = core.list(status);
        return new ResponseEntity(t, HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<CarModel> update(@PathVariable String id, @RequestBody @Valid CarModel car) {
        CarModel t = core.update(id, car);
        return new ResponseEntity<>(t, HttpStatus.ACCEPTED);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        core.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}

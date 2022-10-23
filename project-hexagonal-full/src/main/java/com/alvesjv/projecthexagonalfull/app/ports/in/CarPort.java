package com.alvesjv.projecthexagonalfull.app.ports.in;

import com.alvesjv.projecthexagonalfull.app.domain.model.api.CarModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CarPort {
    public ResponseEntity<CarModel> create(CarModel CarModel);
    public ResponseEntity<CarModel> find(String id);
    public ResponseEntity<List<CarModel>> list(String status);
    public ResponseEntity<CarModel> update(String id, CarModel CarModel);
    public ResponseEntity<?> delete(String id);
}

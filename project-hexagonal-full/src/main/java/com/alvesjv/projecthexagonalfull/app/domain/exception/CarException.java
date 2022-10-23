package com.alvesjv.projecthexagonalfull.app.domain.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CarException extends RuntimeException{

    private HttpStatus status;

    public CarException(String error, HttpStatus status){
        super(error);
        this.status = status;
    }
}

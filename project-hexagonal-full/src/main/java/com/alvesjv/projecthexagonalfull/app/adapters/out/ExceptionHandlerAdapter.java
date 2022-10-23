package com.alvesjv.projecthexagonalfull.app.adapters.out;

import com.alvesjv.projecthexagonalfull.app.domain.exception.CarException;
import com.alvesjv.projecthexagonalfull.app.domain.exception.CustomException;
import com.alvesjv.projecthexagonalfull.app.domain.model.error.CarError;
import com.alvesjv.projecthexagonalfull.app.domain.model.error.FieldError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestControllerAdvice
public class ExceptionHandlerAdapter extends ResponseEntityExceptionHandler {
    private final static String DEFAULT_MSG = "Unexpected error";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customException(CustomException t) {
        CarError carError = new CarError();

        if(ExceptionUtils.getRootCause(t) instanceof CarException){
            CarException te = (CarException) ExceptionUtils.getRootCause(t);

            carError.setMessage(te.getMessage());
            carError.setStatus(te.getStatus().value());

            return new ResponseEntity(carError, te.getStatus());
        }

        carError.setMessage(DEFAULT_MSG);
        carError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(carError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<FieldError>> fieldsException(ConstraintViolationException ex) {
        List<FieldError> errors = new ArrayList<>();

        ex.getConstraintViolations().parallelStream().forEach(constraintViolation->{
            FieldError field = new FieldError();
            field.setMessage(constraintViolation.getMessage());
            field.setValue(String.valueOf(constraintViolation.getInvalidValue()));

            AtomicReference<String> name = new AtomicReference<>();
            constraintViolation.getPropertyPath().forEach(path->{
                name.set(path.getName());
            });

            field.setField(name.get());
            errors.add(field);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().parallelStream().forEach(error->{
            FieldError field = new FieldError();
            field.setField(error.getField());
            field.setValue(String.valueOf(error.getRejectedValue()));
            field.setMessage(error.getDefaultMessage());

            errors.add(field);
        });

        return new ResponseEntity<>(errors, status);
    }

    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> errors = new ArrayList<>();ex.getMessage();

        FieldError field = new FieldError();
        field.setField(ex.getParameterName());
        field.setMessage(ex.getMessage());

        errors.add(field);

        return new ResponseEntity<>(errors, status);
    }
}

package com.example.demo.dao.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class ExceptionTranslator {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public PersistentExceptionResponse handleValidationExceptions(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .parallelStream()
                .map(e -> e.getMessage())
                .collect(Collectors.toList());

        PersistentExceptionResponse error = PersistentExceptionResponse.builder()
                .timestamp(new Date())
                .details(details)
                .build();
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ServiceExceptionResponse handleValidationExceptions(NotFoundException ex) {
        return ServiceExceptionResponse.builder()
                .timestamp(new Date())
                .details(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ServiceExceptionResponse handleValidationExceptions(DataIntegrityViolationException ex) {
        return ServiceExceptionResponse.builder()
                .timestamp(new Date())
                .details(ex.getCause().getMessage() + "; bCoz this product is already added into cart; for changing, do update it!")
                .build();
    }
}
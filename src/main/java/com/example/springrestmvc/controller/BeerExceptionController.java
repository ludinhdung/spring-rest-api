package com.example.springrestmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BeerExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity Exception() {
        return ResponseEntity.notFound().build();
    }
}

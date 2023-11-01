package com.vipin.shoose.controller;

import com.vipin.shoose.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleMyCustomException(CustomException ex) {
        return new ResponseEntity<>("error occurred", HttpStatus.BAD_REQUEST);
    }

}

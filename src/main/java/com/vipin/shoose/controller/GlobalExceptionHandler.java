//package com.vipin.shoose.controller;
//
//import com.vipin.shoose.exception.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.io.IOException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleException(Exception e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//    @ExceptionHandler(IOException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleIOException(IOException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//
//    @ExceptionHandler(ProductImageNotFoundException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleProductImageNotFoundException(ProductImageNotFoundException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//    @ExceptionHandler(OtpInvalidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleOtpInvalidException(OtpInvalidException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//    @ExceptionHandler(MailAlreadyExistException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleMailAlreadyExistException(MailAlreadyExistException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//    @ExceptionHandler(UserBlockedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleUserBlockedException(UserBlockedException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//    @ExceptionHandler(CustomException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleCustomException1(CustomException e, Model model) {
//        model.addAttribute("error", e.getMessage());
//        return "error";
//    }
//
//}

package com.vipin.shoose.exception;

public class MailAlreadyExistException extends RuntimeException{
    public MailAlreadyExistException(String email) {
      super("Email address "+email+ " already registered");
    }
}

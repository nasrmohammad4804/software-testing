package com.nasr.softwaretesting.exception;

public class CustomerRegistrationNotValidException extends RuntimeException{
    public CustomerRegistrationNotValidException(String message) {
        super(message);
    }
}

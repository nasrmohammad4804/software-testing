package com.nasr.softwaretesting.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = CustomerRegistrationNotValidException.class)
    public ResponseEntity<ExceptionTemplate> customerNotFoundException(CustomerRegistrationNotValidException e){

        return ResponseEntity.badRequest()
                .body(new ExceptionTemplate(e.getMessage(),false));
    }
}
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class ExceptionTemplate {

    private Object body;
    private String message;
    private boolean success;

    public ExceptionTemplate(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}

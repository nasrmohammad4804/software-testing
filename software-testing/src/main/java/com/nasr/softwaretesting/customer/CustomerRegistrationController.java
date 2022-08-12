package com.nasr.softwaretesting.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer-registration")
public class CustomerRegistrationController {

    @Autowired
    private CustomerService customerService;

    @PutMapping
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerRegistrationModel model) {

        try {

        Customer customer = customerService.save(model);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customer);
        }catch (Exception e){

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }

    }
}

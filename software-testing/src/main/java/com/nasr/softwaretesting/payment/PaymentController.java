package com.nasr.softwaretesting.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{customerId}")
    public ResponseEntity<String> makePayment(@RequestBody PaymentModel model, @PathVariable String customerId){

        try {
        paymentService.chargeCard(customerId,model);
            return ResponseEntity.ok("card successfully charged");

        }catch (Exception e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

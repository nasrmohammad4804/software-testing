package com.nasr.softwaretesting.payment;

import com.nasr.softwaretesting.customer.Customer;
import com.nasr.softwaretesting.customer.CustomerRepository;
import com.nasr.softwaretesting.customer.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
public class PaymentRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentRepository underTest;

    @Test
    void itShouldSavePayment() {
        // given
        Customer customer = customerRepository.save(Customer.builder().id(UUID.randomUUID().toString()).phoneNumber("09024516594")
                .name("bill").build());

        Payment payment=Payment.builder().amount(new BigDecimal(65L)).description("payment for microservice course")
                .currency(Currency.GBP).customer(customer).source("card123").build();

        // when
        underTest.save(payment);

        //then
        assertThat(underTest.findById(payment.getPaymentId()))
                .isPresent()
                .hasValueSatisfying(p -> assertThat(payment).isEqualTo(p));
    }
}

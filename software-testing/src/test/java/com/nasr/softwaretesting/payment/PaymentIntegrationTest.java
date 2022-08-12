package com.nasr.softwaretesting.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nasr.softwaretesting.customer.Customer;
import com.nasr.softwaretesting.customer.CustomerRegistrationModel;
import com.nasr.softwaretesting.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PaymentIntegrationTest {

    //in integration test we only use mockMvc
    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // given
        String customerId = UUID.randomUUID().toString();
        Customer customer = Customer.builder().id(customerId)
                .name("john")
                .phoneNumber("09031276549")
                .build();

        CustomerRegistrationModel model = new CustomerRegistrationModel(customer);

        // when customer is sent
        ResultActions customerRegistrationResultAction = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getObjectToJson(model)));


        Payment payment = Payment.builder().description("buy microservice and soa course ")
                .source("tok_visa")
                .currency(Currency.USD)
                .amount(new BigDecimal("70"))
                .build();

        //when payment is sent
        ResultActions paymentResultAction = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(getObjectToJson(new PaymentModel(payment)))));

        //then
        customerRegistrationResultAction.andExpect(MockMvcResultMatchers.status().isCreated());
        paymentResultAction.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("card successfully charged"));

        //todo: you should create endpoint for get payment of customer and compare with payment object which sent to paymentController
    }

    public static String getObjectToJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("fail to convert object to json");
            return null;
        }
    }
}

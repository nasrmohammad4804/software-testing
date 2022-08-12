package com.nasr.softwaretesting.customer;

import com.nasr.softwaretesting.payment.PaymentIntegrationTest;
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

import java.util.Objects;

import static com.nasr.softwaretesting.payment.PaymentIntegrationTest.getObjectToJson;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldSaveCustomer() throws Exception {
        // given
        Customer customer = Customer.builder()
                .name("andy")
                .phoneNumber("091322451967")
                .build();

        // when
        ResultActions customerResultAction = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(getObjectToJson(new CustomerRegistrationModel(customer)))));
        //then
                customerResultAction.andExpect(MockMvcResultMatchers.status().isBadRequest());
                customerResultAction.andExpect(MockMvcResultMatchers.content().string("your phone number is not valid "));

    }
}

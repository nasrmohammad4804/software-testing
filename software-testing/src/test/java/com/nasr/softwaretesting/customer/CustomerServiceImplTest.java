package com.nasr.softwaretesting.customer;

import com.nasr.softwaretesting.exception.CustomerRegistrationNotValidException;
import com.nasr.softwaretesting.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    //in unit test we only test this layer everything outside  this layer must be mocked
    //then in this layer we only test service layer and repository layer must be mocked
    @Mock
    private CustomerRepository customerRepository;

    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerServiceImpl(customerRepository);
    }

    //integration test is testing for entire of application

    @Captor
    private ArgumentCaptor<Customer> captor;


    @Test
    void itShouldSaveNewCustomer() {

        // given
        String phoneNumber = "09124316593";


        CustomerRegistrationModel model = new CustomerRegistrationModel(Customer.builder().id(UUID.randomUUID().toString())
                .phoneNumber(phoneNumber).name("javad zare").build());

        given(customerRepository.getCustomerCountByPhoneNumber(phoneNumber)).willReturn(0L);

        try (MockedStatic<PhoneNumberValidator> mockedValidator = mockStatic(PhoneNumberValidator.class)) {
            mockedValidator.when(() -> PhoneNumberValidator.validate(phoneNumber)).thenReturn(true);
            underTest.save(model);
        }

        // when
        //don't find customer with this phoneNumber

        //then
        then(customerRepository).should().save(captor.capture());


        assertThat(captor.getValue())
                .isEqualTo(model.getCustomer());

        then(customerRepository).should(times(1)).save(model.getCustomer());

    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNotValid() {
        // given
        String phoneNumber = "08745419801";
        Customer customer = Customer.builder().phoneNumber(phoneNumber)
                .id(UUID.randomUUID().toString())
                .name("james")
                .build();

        // when
        //then
        try (MockedStatic<PhoneNumberValidator> validatorMockedStatic = mockStatic(PhoneNumberValidator.class)) {
            validatorMockedStatic.when(() -> PhoneNumberValidator.validate(phoneNumber)).thenReturn(false);

            assertThatThrownBy(() -> underTest.save(new CustomerRegistrationModel(customer)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("your phone number is not valid");
        }

        then(customerRepository).should(never()).save(customer);
    }

    @Test
    @DisplayName(value = "it shouldn't save customer when customer already exists ")
    void itShouldNotSaveCustomer() {
        // given
        String phoneNumber = "09124316783";
        Customer customer = Customer.builder().id(UUID.randomUUID().toString())
                .name("javad zare").phoneNumber(phoneNumber).build();

        CustomerRegistrationModel model = new CustomerRegistrationModel(customer);
        when(customerRepository.getCustomerCountByPhoneNumber(phoneNumber)).thenReturn(1L);

        // when
        //then
        try (MockedStatic<PhoneNumberValidator> mockedValidator = mockStatic(PhoneNumberValidator.class)) {
            mockedValidator.when(() -> PhoneNumberValidator.validate(phoneNumber)).thenReturn(true);
            assertThatThrownBy(() -> underTest.save(model)).isInstanceOf(CustomerRegistrationNotValidException.class);
        }
        //finally
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldSaveCustomerWhenCustomerIdIsNull() {
        // given
        String phoneNumber= "09439874129";
        Customer customer = Customer.builder()
                .phoneNumber(phoneNumber)
                .name("anderson")
                .build();

        //when
        given(customerRepository.getCustomerCountByPhoneNumber(phoneNumber)).willReturn(0L);
        try( MockedStatic<PhoneNumberValidator> mockedValidator =  mockStatic(PhoneNumberValidator.class)) {
            mockedValidator.when(() -> PhoneNumberValidator.validate(phoneNumber)).thenReturn(true);
            underTest.save(new CustomerRegistrationModel(customer));
            then(customerRepository).should().save(captor.capture());
        }

        //then
        assertThat(captor.getValue().getId())
                .isNotNull();
    }
}
//we  test service method with @ExtendWith and every dependency in layer need to mock even utility class
//we test utility class with tdd
//we test repository layer with data jpa test
//we test controller layer with integration test and MockMvc note in integration test don't mock service or repository
//although in service connect to external service such as sms provider or stipe and etc ...
//we should use mock this service and use @ConditionalOnProperties mocking implementation for integration test but in production we use original sms provider or stripe
// because maybe network is disconnect  then we can't connect to this api
//and  integration test failed pipeline failed in CI
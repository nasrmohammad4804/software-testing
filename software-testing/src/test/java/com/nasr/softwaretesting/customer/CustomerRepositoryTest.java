package com.nasr.softwaretesting.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

//we add this property because constraint on model don't triggered it means we can save duplicate for unique constraint and set null value for not null constraint
@DataJpaTest(
        properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"}
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    @DisplayName(value = "if customer already exists with specific phoneNumber we expect true else false")
    void itShouldExistsCustomerAlreadyByPhoneNumber() {
        // given -> actual setup
        String phoneNumber="09031261399";
        Customer customer = Customer.builder().id(UUID.randomUUID().toString()).name("mohammad nasr")
                .phoneNumber(phoneNumber).build();


        // when -> method call
        underTest.save(customer);
        Long count = underTest.getCustomerCountByPhoneNumber(phoneNumber);

        //then -> assertion
        assertThat(count).isOne();
    }

    @Test
    @DisplayName(value = "saving customer with random id in db")
    void itShouldSaveCustomer() {
        // given
        String id = UUID.randomUUID().toString();
        Customer customer = Customer.builder().id(id).name("mohammad nasr")
                .phoneNumber("09904316792").build();

        // when
        underTest.save(customer);
        //then
        Optional<Customer> optionalCustomer = underTest.findById(id);

        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo("mohammad nasr");
                });
    }

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // given
        String id = UUID.randomUUID().toString();

        Customer customer = Customer.builder().id(id)
                .phoneNumber("09437310947").build();

        //then
        // when


        assertThatThrownBy(() -> underTest.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("not-null property references a null or transient value");
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // given
        Customer customer=Customer.builder().name("mohammad")
                .id(UUID.randomUUID().toString()).build();

        // when
        //then
        assertThatThrownBy(() -> underTest.save(customer)).isInstanceOf(DataIntegrityViolationException.class);

    }
    @Test
    public void  itShouldNotSaveCustomerWhenPhoneNumberAlreadyExists(){
        String phoneNumber="09024315691";
        Customer customer=Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("john").phoneNumber(phoneNumber).build();

        underTest.save(customer);

        Customer customerTwo=Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("mary").phoneNumber(phoneNumber).build();



        assertThatThrownBy(() -> underTest.save(customerTwo));

    }
}
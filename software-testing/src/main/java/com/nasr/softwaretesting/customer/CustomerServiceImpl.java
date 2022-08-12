package com.nasr.softwaretesting.customer;

import com.nasr.softwaretesting.exception.CustomerRegistrationNotValidException;
import com.nasr.softwaretesting.utils.PhoneNumberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private final CustomerRepository repository;

    @Override
    public Customer save(CustomerRegistrationModel model) {

        if (!PhoneNumberValidator.validate(model.getCustomer().getPhoneNumber()))
            throw new IllegalStateException("your phone number is not valid ");

        Long count = repository.getCustomerCountByPhoneNumber(model.getCustomer().getPhoneNumber());

        if (count != 0)
            throw new CustomerRegistrationNotValidException(String.format("this phoneNumber [%s] already taken", model.getCustomer().getPhoneNumber()));

        if (model.getCustomer().getId() == null)
            model.getCustomer().setId(UUID.randomUUID().toString());

        return repository.save(model.getCustomer());
    }

    @Override
    public Customer getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("dont find any customer with id : " + id));
    }
}

package com.nasr.softwaretesting.customer;

public interface CustomerService {

    Customer  save(CustomerRegistrationModel model);

    Customer getById(String id);
}

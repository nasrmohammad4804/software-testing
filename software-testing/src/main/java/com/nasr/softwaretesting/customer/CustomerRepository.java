package com.nasr.softwaretesting.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    @Query("select count(c.id) from Customer as c where c.phoneNumber = :phoneNumber")
    Long getCustomerCountByPhoneNumber(String phoneNumber);
}

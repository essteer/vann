package com.vann.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.models.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByCustomerEmail(String email);
}



package com.vann.model.service;

import com.vann.model.Customer;
import com.vann.repositories.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public Optional<Customer> findCustomerById(UUID customerUuid) {
        return customerRepo.findById(customerUuid);
    }

    public Customer updateCustomer(UUID customerUuid, Customer updatedCustomer) {
        // Check if the customer exists
        if (!customerRepo.existsById(customerUuid)) {
            throw new IllegalArgumentException("Customer not found");
        }
        // Set the ID of the updated customer
        updatedCustomer.setCustomerUuid(customerUuid);
        // Save the updated customer
        return customerRepo.save(updatedCustomer);
    }

    public void deleteCustomer(UUID customerUuid) {
        customerRepo.deleteById(customerUuid);
    }
}

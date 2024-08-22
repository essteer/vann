package com.vann.service;

import com.vann.model.Customer;
import com.vann.repositories.CustomerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Customer> findAllCustomers() {
        return customerRepo.findAll();
    }

    public Optional<Customer> findCustomerById(UUID customerId) {
        return customerRepo.findById(customerId);
    }

    public Customer updateCustomer(UUID customerId, Customer updatedCustomer) {
        // Check if the customer exists
        if (!customerRepo.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        // Set the ID of the updated customer
        updatedCustomer.setCustomerId(customerId);
        // Save the updated customer
        return customerRepo.save(updatedCustomer);
    }

    public void deleteCustomer(UUID customerId) {
        customerRepo.deleteById(customerId);
    }
}

package com.vann.service;

import com.vann.exceptions.CustomerNotFoundException;
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

    public Customer findCustomerById(UUID customerId) {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        return customerOptional.orElseThrow(() -> 
            new CustomerNotFoundException("Customer with ID '" + customerId + "'' not found"));
    }

    public Customer findCustomerByEmail(String email) {
        Optional<Customer> customerOptional = customerRepo.findByCustomerEmail(email);
        return customerOptional.orElseThrow(() -> 
            new CustomerNotFoundException("Customer with email '" + email + "'' not found"));
    }

    public Customer updateCustomer(UUID customerId, Customer updatedCustomer) {
        if (!customerRepo.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer '" + customerId + "' not found");
        }
        updatedCustomer.setCustomerId(customerId);
        return customerRepo.save(updatedCustomer);
    }

    public void deleteCustomer(UUID customerId) {
        customerRepo.deleteById(customerId);
    }
}

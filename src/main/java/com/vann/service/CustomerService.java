package com.vann.service;

import com.vann.exceptions.FieldConflictException;
import com.vann.exceptions.RecordNotFoundException;
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

    public List<Customer> findAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer findCustomerById(UUID customerId) {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException("Customer with ID '" + customerId + "'' not found"));
    }

    public Customer findCustomerByEmail(String email) {
        Optional<Customer> customerOptional = customerRepo.findByCustomerEmail(email.toLowerCase());
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException("Customer with email '" + email + "'' not found"));
    }

    public Customer saveCustomer(Customer customer) {
        checkForEmailConflict(customer);
        return customerRepo.save(customer);
    }

    public Customer updateCustomer(UUID customerId, Customer updatedCustomer) {
        if (!customerRepo.existsById(customerId)) {
            throw new RecordNotFoundException("Customer '" + customerId + "' not found");
        }
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setCustomerEmail(updatedCustomer.getCustomerEmail().toLowerCase());
        return customerRepo.save(updatedCustomer);
    }

    private void checkForEmailConflict(Customer customer) {
        Optional<Customer> existingCustomerOptional = customerRepo.findByCustomerEmail(customer.getCustomerEmail().toLowerCase());
    
        if (existingCustomerOptional.isPresent() && 
            !existingCustomerOptional.get().getCustomerId().equals(customer.getCustomerId())) {
            throw new FieldConflictException("Customer with email '" + customer.getCustomerName() + "' already exists");
        }
    }

    public void deleteCustomer(UUID customerId) {
        customerRepo.deleteById(customerId);
    }
}

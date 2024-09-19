package com.vann.services;

import java.util.*;

import com.vann.exceptions.*;
import com.vann.models.Customer;
import com.vann.repositories.CustomerRepo;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer createCustomer(String name, String email) {
        Customer customer = new Customer(name, email);
        return saveCustomer(customer);
    }

    public List<Customer> findAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer findCustomerById(UUID customerId) throws RecordNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findById(customerId);
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException("Customer with ID '" + customerId + "'' not found"));
    }

    public Customer findCustomerByEmail(String email) throws RecordNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findByCustomerEmail(email.toLowerCase());
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException("Customer with email '" + email + "'' not found"));
    }

    public Customer saveCustomer(Customer customer) {
        checkForEmailConflict(customer);
        customer.generateIdIfAbsent();
        return customerRepo.save(customer);
    }

    public Customer updateCustomer(UUID customerId, Customer updatedCustomer) throws RecordNotFoundException {
        if (!customerRepo.existsById(customerId)) {
            throw new RecordNotFoundException("Customer with ID '" + customerId + "' not found");
        }
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setCustomerEmail(updatedCustomer.getCustomerEmail());
        return customerRepo.save(updatedCustomer);
    }

    private void checkForEmailConflict(Customer customer) throws FieldConflictException {
        Optional<Customer> existingCustomerOptional = customerRepo.findByCustomerEmail(customer.getCustomerEmail().toLowerCase());
    
        if (existingCustomerOptional.isPresent() && 
            !existingCustomerOptional.get().getCustomerId().equals(customer.getCustomerId())) {
            throw new FieldConflictException("Customer with email '" + customer.getCustomerEmail() + "' already exists");
        }
    }

    public void deleteCustomer(UUID customerId) {
        customerRepo.deleteById(customerId);
    }
}

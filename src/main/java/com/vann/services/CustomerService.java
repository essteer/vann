package com.vann.services;

import java.util.*;
import java.util.regex.Pattern;

import com.vann.exceptions.*;
import com.vann.models.Customer;
import com.vann.repositories.CustomerRepo;
import com.vann.utils.LogHandler;

import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer createCustomer(String name, String email) throws IllegalArgumentException {
        String formattedEmail = email.trim().toLowerCase();
        if (isValidEmail(formattedEmail)) {
            Customer customer = new Customer(name, email);
            return saveCustomer(customer);
        } else {
            LogHandler.invalidAttributeError(Customer.class, "email", email, "Invalid email format");
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public Customer saveCustomer(Customer customer) {
        checkForEmailConflict(customer);
        customer.generateIdIfAbsent();
        return customerRepo.save(customer);
    }

    private void checkForEmailConflict(Customer customer) throws FieldConflictException {
        Optional<Customer> existingCustomerOptional = customerRepo.findByEmail(customer.getEmail().toLowerCase());
    
        if (existingCustomerOptional.isPresent() && 
            !existingCustomerOptional.get().getId().equals(customer.getId())) {
            throw new FieldConflictException("Customer with email '" + customer.getEmail() + "' already exists");
        }
    }

    public List<Customer> findAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer findCustomerById(UUID id) throws RecordNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException("Customer with ID '" + id + "'' not found"));
    }

    public Customer findCustomerByEmail(String email) throws RecordNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findByEmail(email.toLowerCase());
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException("Customer with email '" + email + "'' not found"));
    }

    

    public Customer updateCustomer(UUID id, Customer updatedCustomer) throws RecordNotFoundException {
        if (!customerRepo.existsById(id)) {
            throw new RecordNotFoundException("Customer with ID '" + id + "' not found");
        }
        updatedCustomer.setId(id);
        updatedCustomer.setEmail(updatedCustomer.getEmail());
        return customerRepo.save(updatedCustomer);
    }

    

    public void deleteCustomer(UUID id) {
        customerRepo.deleteById(id);
    }
}

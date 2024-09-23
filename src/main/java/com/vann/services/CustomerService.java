package com.vann.services;

import java.util.*;
import java.util.regex.Pattern;

import com.vann.exceptions.*;
import com.vann.models.Customer;
import com.vann.repositories.CustomerRepo;
import com.vann.utils.LogHandler;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Transactional
    public Customer createOne(Customer potentialCustomer) {
        Customer customer = create(potentialCustomer);
        LogHandler.status201Created(CustomerService.class + " | 1 record created");
        return customer;
    }

    @Transactional
    private Customer create(Customer potentialCustomer) {
        String formattedEmail = potentialCustomer.getEmail().trim().toLowerCase();
        potentialCustomer.setEmail(formattedEmail);
        validateCustomerAttributes(potentialCustomer);
        return saveCustomer(potentialCustomer);
    }

    private void validateCustomerAttributes(Customer customer) throws IllegalArgumentException {
        String name = customer.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(CustomerService.class + " | invalid name | cannot be null or empty");
        }
        String email = customer.getEmail();
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException(CustomerService.class + " | invalid email | email=" + email);
        }
        checkForEmailConflict(customer);
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

    @Transactional
    public Customer saveCustomer(Customer customer) {
        checkForEmailConflict(customer);
        return customerRepo.save(customer);
    }

    private void checkForEmailConflict(Customer customer) throws FieldConflictException {
        Optional<Customer> existingCustomerOptional = customerRepo.findByEmail(customer.getEmail().toLowerCase());
    
        if (existingCustomerOptional.isPresent() && 
            !existingCustomerOptional.get().getId().equals(customer.getId())) {
            throw new FieldConflictException(CustomerService.class + " | record already exists | email=" + customer.getEmail());
        }
    }

    public List<Customer> findAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        logBulkFindOperation(customers, "findAll()");
        return customers;
    }

    private void logBulkFindOperation(List<Customer> customers, String details) {
        if (customers.isEmpty()) {
            LogHandler.status204NoContent(CustomerService.class + " | 0 records | " + details);    
        } else {
            LogHandler.status200OK(CustomerService.class + " | " + customers.size() + " records | " + details);
        }
    }

    public Customer findCustomerById(UUID id) throws RecordNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException(CustomerService.class + " | record not found | id=" + id));
    }

    public Customer findCustomerByEmail(String email) throws RecordNotFoundException {
        Optional<Customer> customerOptional = customerRepo.findByEmail(email.toLowerCase());
        return customerOptional.orElseThrow(() -> 
            new RecordNotFoundException(CustomerService.class + " | record not found | email=" + email));
    }

    @Transactional
    public Customer updateCustomer(UUID id, Customer updatedCustomer) throws RecordNotFoundException {
        Customer existingCustomer = customerRepo.findById(id).orElseThrow(() -> 
            new RecordNotFoundException(CustomerService.class + " | record not found | id=" + id)
        );
        validateCustomerAttributes(updatedCustomer);
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        Customer savedCustomer = saveCustomer(existingCustomer);
        LogHandler.status200OK(CustomerService.class + " | record updated | id=" + id);

        return savedCustomer;
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            LogHandler.status204NoContent(CustomerService.class + " | record deleted | id=" + id);
        }
    }
}

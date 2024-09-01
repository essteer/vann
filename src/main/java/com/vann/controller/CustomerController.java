package com.vann.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vann.exceptions.FieldConflictException;
import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Customer;
import com.vann.service.CustomerService;
import com.vann.utils.LogHandler;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        String methodName = "getAllCustomers()";
        try {
            List<Customer> customers = customerService.findAllCustomers();
            if (customers.isEmpty()) {
                LogHandler.status204NoContent("GET", CustomerController.class, methodName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", CustomerController.class, methodName);
            return ResponseEntity.ok(customers);
        
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CustomerController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable UUID id) {
        String methodName = "getCustomer()";
        try {
            Customer customer = customerService.findCustomerById(id);
            LogHandler.status200OK("GET", CustomerController.class, methodName);
            return ResponseEntity.ok(customer);
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", CustomerController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CustomerController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {
        String methodName = "getCustomerByEmail()";
        try {
            Customer customer = customerService.findCustomerByEmail(email);
            LogHandler.status200OK("GET", CustomerController.class, methodName);
            return ResponseEntity.ok(customer);
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", CustomerController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CustomerController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(
        @RequestParam String name,
        @RequestParam String email
    ) {
        String methodName = "createCustomer()";
        try {
            Customer customer = customerService.createCustomer(name, email);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getCustomerId())
                .toUri();
    
            LogHandler.status201Created("POST", CustomerController.class, methodName);
            return ResponseEntity.created(location).body(customer);
        
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("POST", CustomerController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (FieldConflictException e) {
            LogHandler.status409Conflict("POST", CustomerController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("POST", CustomerController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable UUID id, @RequestBody Customer customer) {
        String methodName = "updateCustomer()";
        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            LogHandler.status200OK("PUT", CustomerController.class, methodName);
            return ResponseEntity.ok(updatedCustomer);
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("PUT", CustomerController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FieldConflictException e) {
            LogHandler.status409Conflict("PUT", CustomerController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("PUT", CustomerController.class, methodName, e.getMessage());
            throw e;
        }
    }
}

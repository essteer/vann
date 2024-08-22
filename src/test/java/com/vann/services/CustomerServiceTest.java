package com.vann.services;

import com.vann.model.Customer;
import com.vann.repositories.CustomerRepo;
import com.vann.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("John Doe");
        customer.setCustomerEmail("john.doe@example.com");

        when(customerRepo.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.saveCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getCustomerName());
        verify(customerRepo, times(1)).save(customer);
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer("John Doe", "john.doe@example.com");
        Customer customer2 = new Customer("Jane Doe", "jane.doe@example.com");
    
        when(customerRepo.findAll()).thenReturn(List.of(customer1, customer2));
    
        List<Customer> customers = customerService.findAllCustomers();
    
        assertEquals(2, customers.size());
        assertTrue(customers.contains(customer1));
        assertTrue(customers.contains(customer2));
        verify(customerRepo, times(1)).findAll();
    }

    @Test
    void testFindCustomerById() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("John Doe");

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findCustomerById(customerId);

        assertEquals("John Doe", foundCustomer.getCustomerName());
        verify(customerRepo, times(1)).findById(customerId);
    }

    @Test
    void testFindCustomerByEmail() {
        String email = "john.doe@example.com";
        Customer customer = new Customer();
        customer.setCustomerEmail(email);
        customer.setCustomerName("John Doe");
    
        when(customerRepo.findByCustomerEmail(email)).thenReturn(Optional.of(customer));
    
        Customer foundCustomer = customerService.findCustomerByEmail(email);
    
        assertEquals("John Doe", foundCustomer.getCustomerName());
        assertEquals(email, foundCustomer.getCustomerEmail());
        verify(customerRepo, times(1)).findByCustomerEmail(email);
    }

    @Test
    void testUpdateCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer();
        existingCustomer.setCustomerId(customerId);
        existingCustomer.setCustomerName("John Doe");
        existingCustomer.setCustomerEmail("john.doe@example.com");
    
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerName("Jane Doe");
        updatedCustomer.setCustomerEmail("jane.doe@example.com");
    
        when(customerRepo.existsById(customerId)).thenReturn(true);
        when(customerRepo.save(updatedCustomer)).thenReturn(updatedCustomer);
    
        Customer result = customerService.updateCustomer(customerId, updatedCustomer);
    
        assertEquals(customerId, result.getCustomerId());
        assertEquals("Jane Doe", result.getCustomerName());
        assertEquals("jane.doe@example.com", result.getCustomerEmail());
        verify(customerRepo, times(1)).existsById(customerId);
        verify(customerRepo, times(1)).save(updatedCustomer);
    }

    @Test
    void testDeleteCustomer() {
        UUID customerId = UUID.randomUUID();

        doNothing().when(customerRepo).deleteById(customerId);

        customerService.deleteCustomer(customerId);

        verify(customerRepo, times(1)).deleteById(customerId);
    }
}

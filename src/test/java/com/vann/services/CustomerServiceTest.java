package com.vann.services;

import com.vann.model.Customer;
import com.vann.repositories.CustomerRepo;
import com.vann.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void testFindCustomerById() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("John Doe");

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));

        Optional<Customer> foundCustomer = customerService.findCustomerById(customerId);

        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getCustomerName());
        verify(customerRepo, times(1)).findById(customerId);
    }

    @Test
    void testDeleteCustomer() {
        UUID customerId = UUID.randomUUID();

        doNothing().when(customerRepo).deleteById(customerId);

        customerService.deleteCustomer(customerId);

        verify(customerRepo, times(1)).deleteById(customerId);
    }
}
